package Servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import DAO.UsuarioDAO;
import Exceptions.DAOException;
import VO.ClaseVO;
import VO.UsuarioVO;

@WebServlet("/cliente/ListarClasesCliente")
public class ListarClasesCliente extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */ 
    public ListarClasesCliente() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Obtener la sesion actual
        HttpSession session = request.getSession(false);
        
        // Obtener el correo del cliente desde la sesion
        UsuarioVO usuario = (UsuarioVO) session.getAttribute("usuario");
        String correoCliente = usuario.getCorreo();

        // Obtener las fechas de inicio y fin del request (si estan presentes)
        String fechaInicioParam = request.getParameter("fechaInicio");
        String fechaFinParam = request.getParameter("fechaFin");
        String accion = request.getParameter("accion");

        LocalDate fechaInicio;
        LocalDate fechaFin;

        // Si las fechas no se reciben, calcular la semana actual
        if (fechaInicioParam == null || fechaFinParam == null) {
            // Obtener la fecha de inicio (lunes de la semana actual)
            fechaInicio = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            // Obtener la fecha de fin (domingo de la semana actual)
            fechaFin = fechaInicio.plusDays(6);
        } else {
            // Parsear las fechas recibidas
            fechaInicio = parseLocalDate(fechaInicioParam);
            fechaFin = parseLocalDate(fechaFinParam);
        }

        if ("siguiente".equals(accion)) {
        	fechaInicio = fechaInicio.plusDays(7);
        	fechaFin = fechaFin.plusDays(7);
        } else if ("anterior".equals(accion)) {
        	fechaInicio = fechaInicio.minusDays(7);
        	fechaFin = fechaFin.minusDays(7);
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // Obtener clases desde el DAO (usando LocalDate)
        List<ClaseVO> clases = new ArrayList<>();
        try {
        	clases = usuarioDAO.listClasesClientePorFecha(correoCliente, fechaInicio, fechaFin);
        } catch (DAOException e){
        	System.out.println(e.getMessage());
        	String eAnterior = (String) request.getSession().getAttribute("error");
        	if (eAnterior != null) {
        		request.getSession().setAttribute("error", eAnterior + "\n" + "Error al recuperar tus clases reservadas\nIntente más tarde");
        	}
        	else {
        		request.getSession().setAttribute("error", "Error al recuperar tus clases reservadas\nIntente más tarde");
        	}
        	request.getRequestDispatcher("/html/cliente/home-cliente.jsp").forward(request, response);
        	return;
        }

        // Establecer atributos para la JSP
        request.setAttribute("clases", clases);
        request.setAttribute("fechaInicio", fechaInicio);
        request.setAttribute("fechaFin", fechaFin);
        request.setAttribute("correoCliente", correoCliente);

        request.getRequestDispatcher("/html/cliente/mis-reservas.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	doGet(request, response);
    }
    
    // Metodo para parsear un String en formato "yyyy-MM-dd" a LocalDate
    private LocalDate parseLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }
}

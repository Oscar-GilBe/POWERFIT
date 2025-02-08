package Servlets;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.ResenaDAO;
import VO.ResenaVO;
import VO.UsuarioVO;
import Exceptions.*;

@WebServlet("/cliente/CrearResena")
public class CrearResena extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrearResena() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// Obtener los parametros del formulario
        String puntuacion_str = request.getParameter("puntuacion");
        String texto = request.getParameter("texto");
        String cliente = request.getParameter("cliente");
        String actividad = request.getParameter("actividad");
        
        if (cliente == null || cliente.isEmpty()) {
        	cliente = ((UsuarioVO) request.getSession().getAttribute("usuario")).getCorreo();
        }
        if (actividad == null || actividad.isEmpty()) {
        	request.getSession().setAttribute("error", "Selecciona la reseña de una actividad existente");
            response.sendRedirect("/POWERFIT/cliente/ListarActividades");
            return;
        }
        if (puntuacion_str == null || puntuacion_str.isEmpty()) {
        	request.getSession().setAttribute("error", "Introduce una puntuacion");
        	request.setAttribute("actividad", actividad);
            request.getRequestDispatcher("/cliente/reseña-crear.jsp");
            return;
        }
        Integer puntuacion = Integer.parseInt(puntuacion_str);
        if (puntuacion < 0 || puntuacion > 5) {
        	request.getSession().setAttribute("error", "La puntuacion no es valida");
        	request.setAttribute("actividad", actividad);
            request.getRequestDispatcher("/cliente/reseña-crear.jsp");
            return;
        }
        if (texto == null || texto.isEmpty()) {
        	request.getSession().setAttribute("error", "Escribe el texto de la reseña");
        	request.setAttribute("actividad", actividad);
            request.getRequestDispatcher("/cliente/reseña-crear.jsp");
            return;
        }
        
        ResenaDAO resenaDAO = new ResenaDAO();
        ResenaVO resenaVO = new ResenaVO(puntuacion, texto, cliente, actividad);

        try {
            resenaDAO.insertResena(resenaVO);

        } catch (ResenaYaExisteException e) {
        	System.out.println(e.getMessage());
            request.getSession().setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/cliente/ListarResenasCliente").forward(request, response);
            return;
        } catch (DAOException e) {
        	System.out.println(e.getMessage());
            request.getSession().setAttribute("error", "Error al crear la reseña\nIntente más tarde");
            request.setAttribute("actividad", actividad);
            request.getRequestDispatcher("/cliente/InfoActividad").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/html/cliente/reseña-creada.jsp?actividad="+actividad).forward(request, response);
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
}

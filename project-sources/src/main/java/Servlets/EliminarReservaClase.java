package Servlets;

import DAO.ClaseDAO;
import DAO.ReservaDAO;
import Exceptions.ClaseNoEncontradaException;
import Exceptions.DAOException;
import Exceptions.ReservaNoEncontradaException;
import Exceptions.ReservaYaExisteException;
import VO.ClaseVO;
import VO.ReservaVO;
import VO.UsuarioVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet("/cliente/EliminarReservaClase")
public class EliminarReservaClase extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarReservaClase() {
        super();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parámetros de la petición
        String actividad = request.getParameter("actividad");
        LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
        LocalTime inicio = LocalTime.parse(request.getParameter("inicio"));
        
        if (actividad == null || fecha == null || inicio == null) {
        	request.getSession().setAttribute("error", "Selecciona una reserva existente");
        	response.sendRedirect("/POWERFIT/cliente/ListarReservas");
        }

        // Obtener el correo del cliente desde la sesión
        HttpSession session = request.getSession();
        UsuarioVO usuario = (UsuarioVO) session.getAttribute("usuario");

        ReservaDAO reservaDAO = new ReservaDAO();

        try {
            // Eliminar la reserva y actualizar las plazas de la clase
            reservaDAO.dropReserva(usuario.getCorreo(), actividad, fecha, inicio);

            // Redirigir al usuario a una página de éxito
            response.sendRedirect("/POWERFIT/html/cliente/reserva-eliminada.html");

        } catch (ReservaNoEncontradaException e) {
        	request.getSession().setAttribute("error", e.getMessage());
        	response.sendRedirect("/POWERFIT/cliente/ListarReservas");

        } catch (ClaseNoEncontradaException e) {
        	request.getSession().setAttribute("error", e.getMessage());
        	response.sendRedirect("/POWERFIT/cliente/ListarReservas");

        } catch (DAOException e) {
        	request.getSession().setAttribute("error", "Error al cancelar la reserva\nIntentalo más tarde");
        	response.sendRedirect("/POWERFIT/cliente/ListarReservas");
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
}


package Servlets;

import DAO.ClaseDAO;
import DAO.ReservaDAO;
import Exceptions.*;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;


@WebServlet("/cliente/ReservarClase")
public class ReservarClase extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReservarClase() {
        super();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener parámetros de la petición
        String actividad = request.getParameter("actividad");
        LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
        LocalTime inicio = LocalTime.parse(request.getParameter("inicio"));

        // Obtener el correo del cliente desde la sesión
        HttpSession session = request.getSession();
        UsuarioVO cliente = (UsuarioVO) session.getAttribute("usuario");

        ClaseDAO claseDAO = new ClaseDAO();
        ReservaDAO reservaDAO = new ReservaDAO();
        if (actividad == null || actividad.isEmpty()) {
			request.getSession().setAttribute("error", "Selecciona una actividad para reservar una clase");
			request.getRequestDispatcher("/cliente/ListarActividades").forward(request, response);
		} else {
	        try {
	            // Crear la reserva si hay plazas disponibles
	            ReservaVO reserva = new ReservaVO(cliente.getCorreo(), actividad, fecha, inicio);
	            reservaDAO.insertReserva(reserva); // Este método ahora maneja la inserción de la reserva y la actualización de plazas.
	
	            // Redirigir al usuario a una página de éxito
	            response.sendRedirect("/POWERFIT/html/cliente/clase-reservada.html");
	
	        } catch (SinPlazasDisponiblesException e) {
	        	// Obtener la fecha de inicio (lunes de la semana actual)
	            LocalDate fechaInicio = fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
	            // Obtener la fecha de fin (domingo de la semana actual)
	            LocalDate fechaFin = fechaInicio.plusDays(6);
	            String redirect = "/POWERFIT/cliente/ListarClasesActividad?actividad=" + actividad + "&fechaInicio=" + fechaInicio + "&fechaFin=" + fechaFin;
	        	session.setAttribute("error", e.getMessage());
	            response.sendRedirect(redirect);
	
	        } catch (ReservaYaExisteException e) {
	        	// Obtener la fecha de inicio (lunes de la semana actual)
	            LocalDate fechaInicio = fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
	            // Obtener la fecha de fin (domingo de la semana actual)
	            LocalDate fechaFin = fechaInicio.plusDays(6);
	            String redirect = "/POWERFIT/cliente/ListarReservas?actividad=" + actividad + "&fechaInicio=" + fechaInicio + "&fechaFin=" + fechaFin;
	        	session.setAttribute("error", e.getMessage());
	            response.sendRedirect(redirect);
	
	        } catch (ClaseNoEncontradaException e) {
	        	// Obtener la fecha de inicio (lunes de la semana actual)
	            LocalDate fechaInicio = fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
	            // Obtener la fecha de fin (domingo de la semana actual)
	            LocalDate fechaFin = fechaInicio.plusDays(6);
	            String redirect = "/POWERFIT/cliente/ListarClasesActividad?actividad=" + actividad + "&fechaInicio=" + fechaInicio + "&fechaFin=" + fechaFin;
	        	session.setAttribute("error", e.getMessage());
	            response.sendRedirect(redirect);
	
	        } catch (DAOException e) {
	        	// Obtener la fecha de inicio (lunes de la semana actual)
	            LocalDate fechaInicio = fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
	            // Obtener la fecha de fin (domingo de la semana actual)
	            LocalDate fechaFin = fechaInicio.plusDays(6);
	            String redirect = "/POWERFIT/cliente/ListarClasesActividad?actividad=" + actividad + "&fechaInicio=" + fechaInicio + "&fechaFin=" + fechaFin;
	        	session.setAttribute("error", "Error al reservar la clase\nIntentalo más tarde");
	            response.sendRedirect(redirect);
	        }
		}
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
}


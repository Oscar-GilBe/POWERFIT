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

import java.util.ArrayList;
import java.util.List;
import DAO.ActividadDAO;
import VO.ClaseVO;
import Exceptions.*;

@WebServlet("/cliente/ListarClasesActividad")
public class ListarClasesActividad extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ListarClasesActividad() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String nombreActividad = request.getParameter("actividad");

        // Obtener las fechas de inicio y fin del request (si están presentes)
        String fechaInicioParam = request.getParameter("fechaInicio");
        String fechaFinParam = request.getParameter("fechaFin");
        
        String accion = request.getParameter("accion");
        
        LocalDate fechaInicio;
        LocalDate fechaFin;

        if (nombreActividad == null || nombreActividad.isEmpty()) {
        	request.getSession().setAttribute("error", "Selecciona una actividad");
            request.getRequestDispatcher("/cliente/ListarActividades").forward(request, response);
            return;
        }

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
        
        ActividadDAO actividadDAO = new ActividadDAO();
        
        List<ClaseVO> clases = new ArrayList<>();
        // Obtener clases desde el DAO (usando LocalDate)
        try {
        	clases = actividadDAO.listClasesActividadPorFecha(nombreActividad, fechaInicio, fechaFin);
        } catch (DAOException e){
        	request.getSession().setAttribute("error", "Error al recuperar las clases de la actividad\nIntente más tarde");
        	request.getRequestDispatcher("/cliente/ListarActividades").forward(request, response);
        	return;
        }
        

        // Establecer atributos para la JSP
        request.setAttribute("clases", clases);
        request.setAttribute("fechaInicio", fechaInicio);
        request.setAttribute("fechaFin", fechaFin);
        request.setAttribute("actividad", nombreActividad);

        request.getRequestDispatcher("/html/cliente/reservar-clase.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	doGet(request, response);
    }
    
    // Método para parsear un String en formato "yyyy-MM-dd" a LocalDate
    private LocalDate parseLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }
}

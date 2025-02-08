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

import DAO.ActividadDAO;
import DAO.SalaDAO;
import VO.ActividadVO;
import DAO.UsuarioDAO;
import VO.ClaseVO;
import DAO.ClaseDAO;
import Exceptions.*;

@WebServlet("/administrador/EliminarActividad")
public class EliminarActividad extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarActividad() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parametros del formulario
        String actividad = request.getParameter("actividad");
        
        ActividadDAO actividadDAO = new ActividadDAO();

        if (actividad == null || actividad.isEmpty()) {
			request.getSession().setAttribute("error", "Selecciona una actividad");
			request.getRequestDispatcher("/administrador/ListarActividades").forward(request, response);
		} else {
	        try {
	            actividadDAO.dropActividad(actividad);
	            
	        } catch (ActividadNoEncontradaException e) {
	            request.getSession().setAttribute("error", e.getMessage());
	            request.getRequestDispatcher("/administrador/ListarActividades").forward(request, response);
	            return;
	        } catch (DAOException e) {
	            request.getSession().setAttribute("error", "Error al eliminar la actividad\nIntentalo más tarde");
	            request.setAttribute("actividad", actividad);
	            request.getRequestDispatcher("/administrador/InfoActividad").forward(request, response);
	            return;
	        }
            request.getRequestDispatcher("/html/administrador/actividad-eliminada.html").forward(request, response);
		}
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
}

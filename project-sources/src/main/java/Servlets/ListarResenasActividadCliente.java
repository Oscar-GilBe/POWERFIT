package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.Base64;

import DAO.ActividadDAO;
import DAO.ResenaDAO;
import VO.ActividadVO;
import VO.ResenaVO;
import Exceptions.*;

@WebServlet("/cliente/ListarResenasActividad")
public class ListarResenasActividadCliente extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */ 
    public ListarResenasActividadCliente() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ActividadDAO dao = new ActividadDAO();
        ActividadVO actividad = null;
		Integer puntuacion = 0;
        List<String> horario = null;
        
        String nombre_actividad = request.getParameter("actividad");
		if (nombre_actividad == null || nombre_actividad.isEmpty()) {
			request.getSession().setAttribute("error", "Selecciona una actividad");
			request.getRequestDispatcher("/cliente/ListarActividades").forward(request, response);
		} else {
            try {
                actividad = dao.getActividad(nombre_actividad);
                byte[] imagen = dao.getImagenActividad(nombre_actividad);
                String imagenBase64 = Base64.getEncoder().encodeToString(imagen);
                request.setAttribute("imagenBase64", imagenBase64);
                // Convertir la imagen a base64
                System.out.println("Actividad cargada.");
            }
            catch (ActividadNoEncontradaException e) {
                request.getSession().setAttribute("error", e.getMessage());
				request.getRequestDispatcher("/cliente/ListarActividades").forward(request, response);
                return;
            }
            catch (DAOException e) {
                request.getSession().setAttribute("error", "Error al recuperar la informacion de la actividad\nIntente más tarde");
				request.getRequestDispatcher("/cliente/ListarActividades").forward(request, response);
                return;
            }
            try {
                puntuacion = dao.puntuacionMedia(actividad.getNombre());
            }
            catch (DAOException e) {
                request.setAttribute("error", "Error al calcular la puntuacion de la actividad\nIntente más tarde");
				request.getRequestDispatcher("/cliente/ListarActividades").forward(request, response);
                return;
            }
            try {
                horario = dao.listHorarios(actividad.getNombre());
            }
            catch (DAOException e) {
                request.setAttribute("error", "Error al obtener el horario de la actividad\nIntente más tarde");
				request.getRequestDispatcher("/cliente/ListarActividades").forward(request, response);
                return;
            }
            request.setAttribute("actividad", actividad);
            request.setAttribute("puntuacion", puntuacion);
            request.setAttribute("horario", horario);
    	
	    	ResenaDAO resenaDAO = new ResenaDAO();
	        List<ResenaVO> lista = null;
	        try {
	            lista = resenaDAO.listResenasPorActividad(nombre_actividad);
	        }
	        catch (DAOException e) {
	        	String eAnterior = (String) request.getSession().getAttribute("error");
	        	if (eAnterior != null) {
	        		request.getSession().setAttribute("error", eAnterior + "\n" + "Error al recuperar las reseñas de la actividad\nIntente más tarde");
	        	}
	        	else {
	        		request.getSession().setAttribute("error", "Error al recuperar las reseñas de la actividad\nIntente más tarde");
	        	}
	        	request.setAttribute("actividad", actividad);
	            request.getRequestDispatcher("/cliente/InfoActividad").forward(request, response);
	            return;
	        }
	
	        request.setAttribute("listaResenas", lista);
	        request.getRequestDispatcher("/html/cliente/info-actividad-resenas.jsp").forward(request, response);
		}
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
}

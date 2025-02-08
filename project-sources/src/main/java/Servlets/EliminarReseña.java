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

@WebServlet("/cliente/EliminarReseña")
public class EliminarReseña extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarReseña() {
        super();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parametros del formulario
        String cliente = request.getParameter("cliente");
        String actividad = request.getParameter("actividad");
        
        if (cliente == null || cliente.isEmpty()) {
        	cliente = ((UsuarioVO) request.getSession().getAttribute("usuario")).getCorreo();
        }
        if (actividad == null || actividad.isEmpty()) {
        	request.getSession().setAttribute("error", "Selecciona la reseña de una actividad existente");
            response.sendRedirect("/POWERFIT/cliente/ListarReseñas");
            return;
        } 

        ResenaDAO resenaDAO = new ResenaDAO();

        try {
            resenaDAO.dropResena(cliente, actividad);

        } catch (ResenaNoEncontradaException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect("/POWERFIT/cliente/ListarReseñas");
            return;
        } catch (DAOException e) {
            request.getSession().setAttribute("error", "Error al eliminar la reseña\nIntentalo más tarde");
            response.sendRedirect("/POWERFIT/cliente/ListarReseñas");
            return;
        }
        request.getRequestDispatcher("/html/cliente/reseña-eliminada.html").forward(request, response);
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
}

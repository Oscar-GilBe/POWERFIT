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

@WebServlet("/cliente/ModificarReseña")
public class ModificarReseña extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModificarReseña() {
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
            response.sendRedirect("/POWERFIT/cliente/ListarResenasCliente");
            return;
        }
        if (puntuacion_str == null || puntuacion_str.isEmpty()) {
        	request.getSession().setAttribute("error", "Introduce una puntuacion");
            response.sendRedirect("/POWERFIT/cliente/ListarResenasCliente");
            return;
        }
        Integer puntuacion = Integer.parseInt(puntuacion_str);
        if (puntuacion < 0 || puntuacion > 5) {
        	request.getSession().setAttribute("error", "La puntuacion no es valida");
        	request.getRequestDispatcher("/html/cliente/reseña-editar.jsp").forward(request, response);
            return;
        }
        if (texto == null || texto.isEmpty()) {
        	request.getSession().setAttribute("error", "Escribe el texto de la reseña");
        	request.getRequestDispatcher("/html/cliente/reseña-editar.jsp").forward(request, response);
            return;
        }
        
        ResenaDAO resenaDAO = new ResenaDAO();
        ResenaVO resenaVO = new ResenaVO(puntuacion, texto, cliente, actividad);

        try {
            resenaDAO.updateResena(resenaVO);

        } catch (ResenaNoEncontradaException e) {
        	System.out.println(e.getMessage());
            request.getSession().setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/html/cliente/mis-reseñas.jsp").forward(request, response);
            return;
        } catch (DAOException e) {
        	System.out.println(e.getMessage());
            request.getSession().setAttribute("error", "Error al guardar la reseña\nIntentalo más tarde");
            request.getRequestDispatcher("/html/cliente/mis-reseñas.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/html/cliente/reseña-modificada.html").forward(request, response);
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
    
}

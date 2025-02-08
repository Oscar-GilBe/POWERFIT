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

import DAO.ResenaDAO;
import VO.ResenaVO;
import VO.UsuarioVO;
import Exceptions.*;

@WebServlet("/cliente/ListarReseñas")
public class ListarReseñasCliente extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */ 
    public ListarReseñasCliente() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResenaDAO dao = new ResenaDAO();
        List<ResenaVO> lista = new ArrayList<>();
        
        UsuarioVO usuario = (UsuarioVO) request.getSession().getAttribute("usuario");
        try {
            lista = dao.listResenasPorCliente(usuario.getCorreo());
        }
        catch (DAOException e) {
        	String eAnterior = (String) request.getSession().getAttribute("error");
        	if (eAnterior != null) {
        		request.getSession().setAttribute("error", eAnterior + "\n" + "Error al recuperar tus reseñas\nIntentalo más tarde");
        	}
        	else {
        		request.getSession().setAttribute("error", "Error al recuperar tus reseñas\nIntentalo más tarde");
        	}
            request.getRequestDispatcher("/html/cliente/home.jsp").forward(request, response);
            return;
        }

        request.setAttribute("listaResenas", lista);
        request.getRequestDispatcher("/html/cliente/mis-reseñas.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
}

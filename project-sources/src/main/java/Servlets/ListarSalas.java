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

import DAO.SalaDAO;
import VO.SalaVO;
import Exceptions.*;

@WebServlet("/administrador/ListarSalas")
public class ListarSalas extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */ 
    public ListarSalas() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SalaDAO dao = new SalaDAO();
        List<SalaVO> lista = null;
        try {
            lista = dao.listSalas();
        }
        catch (DAOException e) {
        	 request.getSession().setAttribute("error", "Error al recuperar la lista de salas\nIntentalos tarde");
             request.getRequestDispatcher("/html/administrador/home.jsp").forward(request, response);
        }
        
        request.setAttribute("listaSalas", lista);
        request.getRequestDispatcher("/html/administrador/listar-salas.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
}
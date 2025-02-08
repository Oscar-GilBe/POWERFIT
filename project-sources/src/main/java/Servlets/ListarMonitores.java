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

import DAO.UsuarioDAO;
import VO.UsuarioVO;
import Exceptions.*;

@WebServlet("/administrador/ListarMonitores")
public class ListarMonitores extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */ 
    public ListarMonitores() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsuarioDAO dao = new UsuarioDAO();
        List<UsuarioVO> lista = new ArrayList<>();
        try {
            lista = dao.listMonitores();
        }
        catch (DAOException e) {
            request.getSession().setAttribute("error", "Error al recuperar la lista de monitores\nIntente más tarde");
            request.getRequestDispatcher("/html/administrador/home.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("listaMonitores", lista);
        request.getRequestDispatcher("/html/administrador/gestionar-monitores.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
}

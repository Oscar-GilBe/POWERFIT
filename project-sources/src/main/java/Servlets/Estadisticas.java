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

import DAO.ActividadDAO;
import DAO.UsuarioDAO;
import Exceptions.*;

@WebServlet("/administrador/Estadisticas")
public class Estadisticas extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Estadisticas() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int nClientes, nMonitores, nActividades;
        ActividadDAO actividadDAO= new ActividadDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        try {
            nClientes = usuarioDAO.countClientes();
            nMonitores = usuarioDAO.countMonitores();
            nActividades = actividadDAO.countActividades();
        }
        catch (DAOException e) {
            request.getSession().setAttribute("error", "Error al obtener las estadísticas\nIntentalo más tarde");
            request.getRequestDispatcher("/html/administrador/home.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("nClientes", nClientes);
        request.setAttribute("nMonitores", nMonitores);
        request.setAttribute("nActividades", nActividades);
        request.getRequestDispatcher("/html/administrador/estadisticas.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
}
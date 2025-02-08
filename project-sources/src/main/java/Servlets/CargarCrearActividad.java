package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;

import DAO.SalaDAO;
import VO.SalaVO;
import DAO.UsuarioDAO;
import VO.UsuarioVO;
import Exceptions.DAOException;

@WebServlet("/administrador/CargarCrearActividad")
public class CargarCrearActividad extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */ 
    public CargarCrearActividad() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	SalaDAO salaDAO = new SalaDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        List<SalaVO> listaSalas = null;
        List<UsuarioVO> listaMonitores = null;
        	
            // Obtener lista de salas
        	try {
        		listaSalas = salaDAO.listSalas();
        	}
        	catch (DAOException e) {
            	request.setAttribute("error", "Error al cargar salas. Intentalo más tarde."); // Mensaje de error
                return;
            }
        	
            // Obtener lista de monitores
        	try {
        		listaMonitores = usuarioDAO.listMonitores();
        	}
            catch (DAOException e) {
            	request.setAttribute("error", "Error al cargar monitores. Intentalo más tarde."); // Mensaje de error
                return;
            }
            
            // Pasar listas al JSP
            request.setAttribute("listaSalas", listaSalas);
            request.setAttribute("listaMonitores", listaMonitores);
            
            List<String> dias = Arrays.asList("LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO");
            request.setAttribute("dias", dias);
            
            // Redirigir al JSP con todos los datos cargados
            request.getRequestDispatcher("/html/administrador/crear-actividad.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
}

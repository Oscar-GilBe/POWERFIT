package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.UsuarioDAO;
import VO.TipoUsuario;
import VO.UsuarioVO;
import Exceptions.*;
import Util.PasswordUtil;

@WebServlet("/administrador/DarAltaMonitor")
public class DarAltaMonitor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DarAltaMonitor() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuarioDAO dao = new UsuarioDAO();
        UsuarioVO usuario = null;
        
        String email = request.getParameter("email");
        
		if (email == null || email.isEmpty()) {
			request.getSession().setAttribute("error", "El correo electrónico es obligatorio");
			request.getRequestDispatcher("/html/administrador/agregar-monitor.jsp").forward(request, response);
			return;
		}
		
		// Validar que el email termina con @powerfit.com
	    if (!email.endsWith("@powerfit.com")) {
	        request.getSession().setAttribute("error", "El correo electrónico debe ser corporativo y, por tanto, terminar con @powerfit.com");
	        request.getRequestDispatcher("/html/administrador/agregar-monitor.jsp").forward(request, response);
	        return;
	    }

	    String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String contrasena = request.getParameter("password");
	    
        HttpSession session = request.getSession();
	    if (contrasena == null || contrasena.isEmpty()) {
			session.setAttribute("error", "El campo contraseña es obligatorio");
			request.getRequestDispatcher("/html/administrador/agregar-monitor.jsp").forward(request, response);
			return;
		}  
		if (nombre == null || nombre.isEmpty()) {
			session.setAttribute("error", "El campo nombre es obligatorio");
			request.getRequestDispatcher("/html/administrador/agregar-monitor.jsp").forward(request, response);
			return;
		} 
		if (apellidos == null || apellidos.isEmpty()) {
			session.setAttribute("error", "El campo apellidos es obligatorio");
			request.getRequestDispatcher("/html/administrador/agregar-monitor.jsp").forward(request, response);
			return;
		}  
	    
        usuario = new UsuarioVO(email, nombre, apellidos, PasswordUtil.hashPassword(contrasena), TipoUsuario.MONITOR);
        try {
            dao.insertUsuario(usuario);
        }
        catch (UsuarioYaExisteException e) {    // El usuario ya existe
            request.getSession().setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/html/administrador/agregar-monitor.jsp").forward(request, response);
            return;
        }
        catch (DAOException e) {
        	// Manejo de la excepción DAOException
        	request.getSession().setAttribute("error", "Error al registrar el monitor en la base de datos\nIntente más tarde");
        	request.getRequestDispatcher("/html/administrador/agregar-monitor.jsp").forward(request, response);
        	return;
    	}
        request.getRequestDispatcher("/administrador/ListarMonitores").forward(request, response);
	}
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
}

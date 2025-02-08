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

@WebServlet("/CrearCuenta")
public class CrearCuenta extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrearCuenta() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse rsesponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuarioDAO dao = new UsuarioDAO();
        UsuarioVO usuario = null;
        
        HttpSession session = request.getSession();
        
        String email = request.getParameter("email");
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String contrasena = request.getParameter("password");
        
        if (email == null || email.isEmpty()) {
			session.setAttribute("error", "El campo email es obligatorio");
			request.getRequestDispatcher("/html/registrarse.jsp").forward(request, response);
			return;
		} 
		if (contrasena == null || contrasena.isEmpty()) {
			session.setAttribute("error", "El campo contraseña es obligatorio");
			request.getRequestDispatcher("/html/registrarse.jsp").forward(request, response);
			return;
		}  
		if (nombre == null || nombre.isEmpty()) {
			session.setAttribute("error", "El campo nombre es obligatorio");
			request.getRequestDispatcher("/html/registrarse.jsp").forward(request, response);
			return;
		} 
		if (apellidos == null || apellidos.isEmpty()) {
			session.setAttribute("error", "El campo apellidos es obligatorio");
			request.getRequestDispatcher("/html/registrarse.jsp").forward(request, response);
			return;
		}  
        
        usuario = new UsuarioVO(email, nombre, apellidos, PasswordUtil.hashPassword(contrasena), TipoUsuario.CLIENTE);
        try {
            dao.insertUsuario(usuario);
        }
        catch (UsuarioYaExisteException e) {    // El usuario ya existe
        	request.getSession().setAttribute("error", e.getMessage() + "\nPrueba a iniciar sesion");
			request.getRequestDispatcher("/html/registrarse.jsp").forward(request, response);
            return;
        }
        catch (DAOException e) {
        	request.getSession().setAttribute("error", "Error al crear la cuenta\nIntente más tarde");
			request.getRequestDispatcher("/html/registrarse.jsp").forward(request, response);
            return;
        }
        usuario.setContrasena(null);    // Borrar contrasena por razones de seguridad
        request.getSession().setAttribute("usuario", usuario); // Guardar usuario
        request.getRequestDispatcher("/html/cliente/home.jsp").forward(request, response);
    }    
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
}

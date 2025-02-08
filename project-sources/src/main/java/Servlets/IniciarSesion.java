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

@WebServlet("/IniciarSesion")
public class IniciarSesion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public IniciarSesion() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuarioDAO dao = new UsuarioDAO();
        UsuarioVO usuario = null;
        HttpSession session = request.getSession();

        String email = request.getParameter("email");
        String contrasena = request.getParameter("password");
		if (email == null || email.isEmpty()) {
			session.setAttribute("error", "El campo email es obligatorio");
			request.getRequestDispatcher("/html/iniciar-sesion.jsp").forward(request, response);
			return;
		} 
		if (contrasena == null || contrasena.isEmpty()) {
			session.setAttribute("error", "El campo contraseña es obligatorio");
			request.getRequestDispatcher("/html/iniciar-sesion.jsp").forward(request, response);
			return;
		}  
		
        try {
            usuario = dao.getUsuario(request.getParameter("email"));
        }
        catch (UsuarioNoEncontradoException e) {
            session.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("/html/iniciar-sesion.jsp").forward(request, response);
            return;
        }
        catch (DAOException e) {
        	session.setAttribute("error", "Error al iniciar sesion\nIntentalo más tarde");
			request.getRequestDispatcher("/html/iniciar-sesion.jsp").forward(request, response);
            return;
        }

        if (PasswordUtil.checkPassword(contrasena, usuario.getContrasena())) {  // Contraseña correcta
            usuario.setContrasena(null);    // Borrar contraseña por razones de seguridad
			session.setAttribute("usuario", usuario); // Guardar usuario en la sesión

			// Comprobar si existe una URL original guardada en la sesión
			String originalURL = (String) session.getAttribute("originalURL");
			//System.out.println(originalURL);
			if (originalURL != null) {
				// Si existe una URL original, redirigir a esa página
				session.removeAttribute("originalURL"); // Limpiar la URL de la sesión
				//response.sendRedirect(originalURL);
				request.getRequestDispatcher(originalURL).forward(request, response);
				return;
			} else {
				// Si no existe URL original, redirigir según el tipo de usuario
				switch (usuario.getTipo()) {
				case CLIENTE:
					request.getRequestDispatcher("/html/cliente/home.jsp").forward(request, response);
					break;
				case MONITOR:
					request.getRequestDispatcher("/html/monitor/home.jsp").forward(request, response);
					break;
				case ADMINISTRADOR: 
					request.getRequestDispatcher("/html/administrador/home.jsp").forward(request, response);
					break;
				}
			}
		} else {    // Contraseña incorrecta
			session.setAttribute("error", "Contraseña no válida");
			request.getRequestDispatcher("/html/iniciar-sesion.jsp").forward(request, response);
		}
	}
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
}

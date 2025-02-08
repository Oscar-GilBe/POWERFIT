package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import DAO.UsuarioDAO;
import VO.TipoUsuario;
import VO.UsuarioVO;
import VO.ClaseVO;
import Exceptions.*;

@WebServlet("/cliente/EliminarCuenta")
public class EliminarCuentaCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarCuentaCliente() {
        super();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// Obtener la sesion actual
        HttpSession session = request.getSession(false);
        
        // Obtener el correo del cliente desde la sesion
        UsuarioVO usuario = (UsuarioVO) session.getAttribute("usuario");
        String correo = usuario.getCorreo();
        
        if (correo == null) {
            // Si no se proporciona el correo del monitor, redirigir con un mensaje de error
            request.getSession().setAttribute("error", "El correo del cliente es requerido para eliminar la cuenta");
            request.getRequestDispatcher("/html/cliente/home-cliente.jsp").forward(request, response);
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        try {
            usuarioDAO.dropUsuario(correo);
        } catch (UsuarioNoEncontradoException e) {
            // Manejar el caso en que el cliente no exista
            request.getSession().setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/html/iniciar-sesion.jsp").forward(request, response);
            return;
        } catch (DAOException e) {
            // Manejar errores generales de la base de datos
            request.getSession().setAttribute("error", "Error al eliminar la cuenta\nIntentalo más tarde");
            request.getRequestDispatcher("/html/cliente/home.jsp").forward(request, response);
            return;
        }
        session.invalidate();
        request.getRequestDispatcher("/html/cuenta-eliminada.html").forward(request, response);
	}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
}

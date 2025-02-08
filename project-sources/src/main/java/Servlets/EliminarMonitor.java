package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import DAO.UsuarioDAO;
import VO.TipoUsuario;
import VO.ClaseVO;
import Exceptions.*;

@WebServlet("/administrador/EliminarMonitor")
public class EliminarMonitor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarMonitor() {
        super();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el id del monitor desde el request
        String correo = request.getParameter("correo");
        
        if (correo == null) {
            // Si no se proporciona el correo del monitor, redirigir con un mensaje de error
            request.getSession().setAttribute("error", "El correo del monitor es requerido para eliminar su cuenta");
            request.getRequestDispatcher("/administrador/ListarMonitores").forward(request, response);
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        try {
            // Comprobar si hay actividades asociadas al monitor
            List<ClaseVO> clasesMonitor = usuarioDAO.listClasesMonitor(correo);

            if (!clasesMonitor.isEmpty()) {
                // Si hay clases para el monitor, mostrar un error
                request.getSession().setAttribute("error", "No se puede eliminar el monitor porque tiene clases asociadas");
                request.getRequestDispatcher("/administrador/ListarMonitores").forward(request, response);
            } else {
                // Si no hay clases, proceder con la eliminación
                usuarioDAO.dropUsuario(correo);

                // Redirigir a una página de éxito o listado de salas
                response.sendRedirect("/POWERFIT/html/administrador/monitor-eliminado.html");
            }

        } catch (UsuarioNoEncontradoException e) {
            // Manejar el caso en que el monitor no exista
            request.getSession().setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/administrador/ListarMonitores").forward(request, response);
        } catch (DAOException e) {
            // Manejar errores generales de la base de datos
            request.getSession().setAttribute("error", "Error al eliminar la cuenta del monitor\nIntentalo más tarde");
            request.getRequestDispatcher("/administrador/ListarMonitores").forward(request, response);
        }
	}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
}

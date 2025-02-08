package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/administrador/CerrarSesion")
public class CerrarSesionAdmin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CerrarSesionAdmin() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener la sesion actual, si existe
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Invalidar la sesion actual
            session.invalidate();
        }

        // Redirigir al usuario a la pagina de sesion cerrada
        response.sendRedirect("/POWERFIT/html/sesion-cerrada-administrador.html");
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
}
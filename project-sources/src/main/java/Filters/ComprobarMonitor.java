package Filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

import DAO.UsuarioDAO;
import VO.TipoUsuario;
import VO.UsuarioVO;
import Exceptions.*;

@WebFilter({
	"/html/monitor/*",
	"/monitor/*"
})
public class ComprobarMonitor implements Filter {

    public void init(FilterConfig fConfig) throws ServletException {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Intentamos obtener la sesión sin crear una nueva si no existe
        HttpSession session = req.getSession(false);

        // Si no hay sesión, creamos una nueva para almacenar el mensaje de error
        if (session == null) {
            session = req.getSession(true);
            String errorMessage = "Debes iniciar sesión para acceder a esta página.";
            session.setAttribute("error", errorMessage);

            // Guardar la URL original a la que el usuario intentaba acceder
            String originalURL = obtenerURLCompleta(req);
            session.setAttribute("originalURL", originalURL);

            // Redirigir al usuario a la página de inicio de sesión
            res.sendRedirect("/POWERFIT/html/iniciar-sesion.jsp");
            return;
        }

        // Obtenemos el usuario de la sesión
        UsuarioVO user = (UsuarioVO) session.getAttribute("usuario");

        if (user == null) {
            String errorMessage = "Debes iniciar sesión para acceder a esta página.";
            session.setAttribute("error", errorMessage);

            // Guardar la URL original a la que el usuario intentaba acceder
            String originalURL = obtenerURLCompleta(req);
            session.setAttribute("originalURL", originalURL);

            // Redirigir al usuario a la página de inicio de sesión
            res.sendRedirect("/POWERFIT/html/iniciar-sesion.jsp");
            return;
        } else if (user.getTipo() != TipoUsuario.MONITOR) {
            // Si el usuario existe pero no es del tipo MONITOR
            String errorMessage = "Debes ser Monitor para acceder a esta página.";
            session.setAttribute("error", errorMessage);

            // Redirigir al usuario a la página de inicio de sesión
            res.sendRedirect("/POWERFIT/html/iniciar-sesion.jsp");
            return;
        } else {
            // El usuario está autenticado y es Monitor, permitimos el acceso
            chain.doFilter(request, response);
        }
    }

    public void destroy() {}

    /**
     * Obtiene la URL completa que el usuario estaba intentando acceder,
     * eliminando el prefijo http://localhost:8080/POWERFIT si está presente.
     */
    private String obtenerURLCompleta(HttpServletRequest req) {
        // Obtener la URL original completa
        String originalURL = req.getRequestURL().toString();
        if (req.getQueryString() != null) {
            originalURL += "?" + req.getQueryString(); // Si hay parámetros de consulta, agregarlos
        }

        // Definir la parte que deseas eliminar
        String prefixToRemove = "http://localhost:8080/POWERFIT";

        // Comprobar si la URL comienza con el prefijo y eliminarlo
        if (originalURL.startsWith(prefixToRemove)) {
            originalURL = originalURL.replaceFirst(prefixToRemove, ""); // Eliminar el prefijo
        }

        return originalURL;
    }
}

package Servlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.SalaDAO;
import VO.ActividadVO;
import VO.SalaVO;
import Exceptions.SalaNoEncontradaException;
import Exceptions.SalaYaExisteException;
import Exceptions.DAOException;

@WebServlet("/administrador/EliminarSala")
public class EliminarSala extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarSala() {
        super();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el numero de la sala desde el request
        String numeroSalaStr = request.getParameter("numeroSala");
        
        if (numeroSalaStr == null) {
            // Si no se proporciona el numero de sala, redirigir con un mensaje de error
            request.getSession().setAttribute("error", "El numero de la sala es requerido.");
            request.getRequestDispatcher("/administrador/ListarSalas.jsp").forward(request, response);
            return;
        }

        Integer numeroSala = Integer.parseInt(numeroSalaStr);
        SalaDAO salaDAO = new SalaDAO();
        
        try {
            // Comprobar si hay actividades asociadas a la sala
            List<ActividadVO> actividadesEnSala = salaDAO.listActividadesSala(numeroSala);

            if (!actividadesEnSala.isEmpty()) {
                // Si hay actividades en la sala, mostrar un error
                request.getSession().setAttribute("error", "No se puede eliminar la sala porque tiene actividades asociadas.");
                request.getRequestDispatcher("/administrador/ListarSalas").forward(request, response);
            } else {
                // Si no hay actividades, proceder con la eliminacion
                salaDAO.dropSala(numeroSala);

                // Redirigir a una pagina de exito o listado de salas
                response.sendRedirect("/POWERFIT/html/administrador/sala-eliminada.html");
            }

        } catch (SalaNoEncontradaException e) {
            // Manejar el caso en que la sala no exista
            request.getSession().setAttribute("error", "No se encontro la sala con el numero: " + numeroSala);
            request.getRequestDispatcher("/administrador/ListarSalas").forward(request, response);
        } catch (DAOException e) {
            // Manejar errores generales de la base de datos
            request.getSession().setAttribute("error", "Error en la base de datos: " + e.getMessage());
            request.getRequestDispatcher("/administrador/ListarSalas").forward(request, response);
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
}

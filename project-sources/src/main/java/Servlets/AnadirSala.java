package Servlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.SalaDAO;
import VO.SalaVO;
import Exceptions.SalaNoEncontradaException;
import Exceptions.SalaYaExisteException;
import Exceptions.DAOException;

@WebServlet("/administrador/AnadirSala")
public class AnadirSala extends HttpServlet {

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnadirSala() {
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
            request.getRequestDispatcher("/html/administrador/anadir-sala.jsp").forward(request, response);
            return;
        }

        Integer numeroSala = Integer.parseInt(numeroSalaStr);
        SalaDAO salaDAO = new SalaDAO();
        
        try {
            SalaVO sala = new SalaVO(numeroSala);   // Crear la sala
            salaDAO.insertSala(sala);   // Insertar la sala
        } catch (SalaYaExisteException e) {
            // Manejar el caso en que la sala ya exista
            request.getSession().setAttribute("error", "Ya existe la sala con el numero: " + numeroSala);
            request.getRequestDispatcher("/html/administrador/anadir-sala.jsp").forward(request, response);
        } catch (DAOException e) {
            // Manejar errores generales de la base de datos
            request.getSession().setAttribute("error", "Error en la base de datos: " + e.getMessage());
            request.getRequestDispatcher("/html/administrador/anadir-sala.jsp").forward(request, response);
        }
        // Todo ha ido bien, vuelve a listar salas
        request.getRequestDispatcher("/administrador/ListarSalas").forward(request, response);
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
}

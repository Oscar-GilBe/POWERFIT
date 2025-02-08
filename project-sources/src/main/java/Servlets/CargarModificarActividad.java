package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DAO.ActividadDAO;
import DAO.SalaDAO;
import DAO.UsuarioDAO;
import VO.ActividadVO;
import VO.SalaVO;
import VO.UsuarioVO;
import Exceptions.DAOException;

@WebServlet("/administrador/CargarModificarActividad")
public class CargarModificarActividad extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */ 
    public CargarModificarActividad() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String nombreActividad = request.getParameter("actividad");
        if (nombreActividad == null || nombreActividad.isEmpty()) {
            request.setAttribute("error", "Nombre de actividad no especificado.");
            request.getRequestDispatcher("/administrador/ListarActividades").forward(request, response);
            return;
        }
    	
        ActividadDAO actividadDAO = new ActividadDAO();
    	SalaDAO salaDAO = new SalaDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        try {
            // Cargar datos de la actividad
            ActividadVO actividad = actividadDAO.getActividad(nombreActividad);
            request.setAttribute("actividad", actividad);

            // Lista de horarios por día en un Map
            List<String> horariosSinFormato = actividadDAO.listHorarios(nombreActividad);
            Map<String, List<String>> horariosPorDia = agruparHorariosPorDia(horariosSinFormato);
            request.setAttribute("horariosPorDia", horariosPorDia);

            // Cargar lista de salas
            List<SalaVO> listaSalas = salaDAO.listSalas();
            request.setAttribute("listaSalas", listaSalas);

            // Cargar lista de monitores
            List<UsuarioVO> listaMonitores = usuarioDAO.listMonitores();
            request.setAttribute("listaMonitores", listaMonitores);

            // Pasar lista de días de la semana al JSP
            List<String> dias = Arrays.asList("LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO");
            request.setAttribute("dias", dias);

            // Redirigir al JSP de edición
            request.getRequestDispatcher("/html/administrador/modificar-actividad.jsp").forward(request, response);

        } catch (DAOException e) {
            request.getSession().setAttribute("error", "Error al cargar la información de la actividad para modificarla. Inténtalo más tarde.");
            request.getRequestDispatcher("/administrador/ListarActividades").forward(request, response);
        }
    }
    
    // Método para agrupar los horarios por día de la semana
    private Map<String, List<String>> agruparHorariosPorDia(List<String> horariosSinFormato) {
        Map<String, List<String>> horariosPorDia = new HashMap<>();
        
        for (String horario : horariosSinFormato) {
            String[] partes = horario.split(", ");
            if (partes.length > 1) {
                String dia = partes[0].trim();
                String[] horas = partes[1].split("-");
                String horaInicio = formatearHora(horas[0].trim());
                String horaFin = horas.length > 1 ? formatearHora(horas[1].trim()) : "";

                // Lista de horarios para el día actual
                horariosPorDia.putIfAbsent(dia, new ArrayList<>());
                horariosPorDia.get(dia).add(horaInicio + " - " + horaFin);
            }
        }
        return horariosPorDia;
    }

    private String formatearHora(String hora) {
        String[] partes = hora.split(":");
        if (partes.length >= 2) {
            return String.format("%02d:%02d", Integer.parseInt(partes[0]), Integer.parseInt(partes[1]));
        }
        return hora;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

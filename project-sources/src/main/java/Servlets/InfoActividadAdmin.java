package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import VO.ActividadVO;
import VO.TipoUsuario;
import VO.UsuarioVO;
import DAO.ActividadDAO;
import Exceptions.ActividadNoEncontradaException;
import Exceptions.DAOException;
import java.util.Base64;

import java.util.ArrayList;
import java.util.List;

@WebServlet("/administrador/InfoActividad")
public class InfoActividadAdmin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InfoActividadAdmin() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ActividadDAO dao = new ActividadDAO();
        ActividadVO actividad = null;
		Integer puntuacion = 0;
        List<String> horario = new ArrayList<>();
        
        System.out.println("Dentro del DOGET de InfoActividadAdmin.");
        String nombre_actividad = request.getParameter("actividad");
		if (nombre_actividad == null || nombre_actividad.isEmpty()) {
			request.getSession().setAttribute("error", "Selecciona una actividad");
			request.getRequestDispatcher("/administrador/ListarActividades").forward(request, response);
		} else {
            try {
                actividad = dao.getActividad(nombre_actividad);
                byte[] imagen = dao.getImagenActividad(nombre_actividad);
                String imagenBase64 = Base64.getEncoder().encodeToString(imagen);
                request.setAttribute("imagenBase64", imagenBase64);
                // Convertir la imagen a base64
                System.out.println("Actividad cargada.");
            }
            catch (ActividadNoEncontradaException e) {
                request.getSession().setAttribute("error", e.getMessage());
				request.getRequestDispatcher("/administrador/ListarActividades").forward(request, response);
                return;
            }
            catch (DAOException e) {
                request.getSession().setAttribute("error", "Error al recuperar la informacion de la actividad\nIntente más tarde");
				request.getRequestDispatcher("/administrador/ListarActividades").forward(request, response);
                return;
            }
            try {
                puntuacion = dao.puntuacionMedia(actividad.getNombre());
                System.out.println("Puntuación cargada.");
            }
            catch (DAOException e) {
                request.setAttribute("error", "Error al calcular la puntuacion de la actividad\nIntente más tarde");
				request.getRequestDispatcher("/administrador/ListarActividades").forward(request, response);
                return;
            }
            try {
            	List<String> horariosSinFormato = dao.listHorarios(actividad.getNombre()); // Lista de horarios sin formatear
                horario = formatearHorarios(horariosSinFormato);
                System.out.println(horario);
                System.out.println("Horarios cargados.");
            }
            catch (DAOException e) {
                request.setAttribute("error", "Error al obtener el horario de la actividad\nIntente más tarde");
				request.getRequestDispatcher("/administrador/ListarActividades").forward(request, response);
                return;
            }
            
            request.setAttribute("actividad", actividad);
            request.setAttribute("puntuacion", puntuacion);
            request.setAttribute("horario", horario);
            System.out.println(horario);

            request.getRequestDispatcher("/html/administrador/info-actividad.jsp").forward(request, response);
            System.out.println("Redirección completada.");
		}
	}
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
    
    private List<String> formatearHorarios(List<String> horariosSinFormato) {
        List<String> horariosFormateados = new ArrayList<>();

        for (String horario : horariosSinFormato) {
            // El formato es ejemplo: "jueves, 15:00:00-16:00:00"
            String[] partes = horario.split(", ");
            if (partes.length > 1) {
                String dia = partes[0].trim(); // Día de la semana
                String[] horas = partes[1].split("-");

                // Formatear horas
                String horaInicio = formatearHora(horas[0].trim());
                String horaFin = horas.length > 1 ? formatearHora(horas[1].trim()) : ""; // Manejar caso donde no haya hora de fin

                // Agregar a la lista en formato deseado
                horariosFormateados.add(dia + ", " + horaInicio + (horaFin.isEmpty() ? "" : " - " + horaFin));
            }
        }

        // Ordenar los horarios por día y luego por hora de inicio
        horariosFormateados.sort((h1, h2) -> {
            int diaComparacion = ordenarDias(h1.split(", ")[0], h2.split(", ")[0]);
            if (diaComparacion != 0) {
                return diaComparacion; // Ordenar por día
            }
            // Comparar las horas (horaInicio de cada String)
            return h1.split(", ")[1].split(" - ")[0].compareTo(h2.split(", ")[1].split(" - ")[0]);
        });

        return horariosFormateados;
    }
    
    private int ordenarDias(String dia1, String dia2) {
        String[] dias = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"};
        int index1 = java.util.Arrays.asList(dias).indexOf(dia1.toUpperCase());
        int index2 = java.util.Arrays.asList(dias).indexOf(dia2.toUpperCase());
        return Integer.compare(index1, index2);
    }

    private String formatearHora(String hora) {
        // Asumiendo que la hora viene en un formato como "HH:mm:ss"
        String[] partes = hora.split(":");
        if (partes.length >= 2) {
            // Asegurarse de que tenga al menos horas y minutos
            return String.format("%02d:%02d", Integer.parseInt(partes[0]), Integer.parseInt(partes[1]));
        }
        return hora; // Retornar original si no se puede formatear
    }
    
}

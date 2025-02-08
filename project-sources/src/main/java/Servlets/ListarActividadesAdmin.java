package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import DAO.ActividadDAO;
import VO.ActividadVO;
import Exceptions.*;

@WebServlet("/administrador/ListarActividades")
public class ListarActividadesAdmin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */   
    public ListarActividadesAdmin() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActividadDAO dao = new ActividadDAO();
        List<ActividadVO> lista = new ArrayList<>();
        
        System.out.println("En el servlet");
        try {
            lista = dao.listActividades();

            String ordenarPor = request.getParameter("ordenarPor");
            String searchQuery = request.getParameter("search"); // Obtener el parámetro de búsqueda

            // Filtrar la lista de actividades si hay una búsqueda
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            	System.out.println("Usando la barra de búsqueda");
                String searchLower = searchQuery.toLowerCase(); // Convertir a minúsculas para hacer la búsqueda insensible a mayúsculas
                lista = lista.stream()
                        .filter(actividad -> actividad.getNombre().toLowerCase().contains(searchLower) ||
                                             actividad.getGrupo_muscular().toLowerCase().contains(searchLower) ||
                                             actividad.getDificultad().toLowerCase().contains(searchLower) ||
                                             String.valueOf(actividad.getSala()).contains(searchLower))
                        .collect(Collectors.toList());
                request.setAttribute("searchQuery", searchQuery); // Pasar el término de búsqueda para mostrarlo en la vista
            }

            if ((ordenarPor == null || ordenarPor.isEmpty())) {
                // Carga inicial sin filtros o búsqueda
            	ordenarPor = "nombre"; // Orden predeterminado
            	System.out.println("Carga sin filtros, ordenando por nombre");
            }
            switch (ordenarPor) {
                case "grupo_muscular":
                    // Agrupar por grupo muscular
                    Map<String, List<ActividadVO>> actividadesPorGrupoMuscular = lista.stream()
                        .collect(Collectors.groupingBy(ActividadVO::getGrupo_muscular));
                    request.setAttribute("agrupadas", true);  
                    request.setAttribute("grupo", actividadesPorGrupoMuscular);
                    break;
                
                case "dificultad":
                    // Crear un LinkedHashMap para mantener el orden "FACIL", "MEDIA", "DIFICIL"
                    Map<String, List<ActividadVO>> actividadesPorDificultad = new LinkedHashMap<>();
                    // Definir el orden deseado
                    String[] ordenDificultad = {"FACIL", "MEDIA", "DIFICIL"};
                    
                    // Agrupar por dificultad y ordenar manualmente
                    Map<String, List<ActividadVO>> agrupadoPorDificultad = lista.stream()
                        .collect(Collectors.groupingBy(ActividadVO::getDificultad));

                    // Insertar las dificultades en el orden deseado
                    for (String dificultad : ordenDificultad) {
                        if (agrupadoPorDificultad.containsKey(dificultad)) {
                            actividadesPorDificultad.put(dificultad, agrupadoPorDificultad.get(dificultad));
                        }
                    }

                    request.setAttribute("agrupadas", true);  
                    request.setAttribute("grupo", actividadesPorDificultad);
                    break;

                case "puntuacion":
                    // Crear un TreeMap para ordenar por puntuación de 5 a 0
                    Map<Float, List<ActividadVO>> actividadesPorPuntuacion = new TreeMap<>(Collections.reverseOrder());

                    // Agrupar por puntuación
                    for (ActividadVO actividad : lista) {
                        float puntuacion = dao.puntuacionMedia(actividad.getNombre());

                        actividadesPorPuntuacion
                            .computeIfAbsent(puntuacion, k -> new ArrayList<>())
                            .add(actividad);
                    }

                    request.setAttribute("agrupadas", true);  
                    request.setAttribute("grupo", actividadesPorPuntuacion);
                    break;

                case "sala":
                    // Crear un TreeMap para que las salas estén ordenadas de forma creciente
                    Map<Integer, List<ActividadVO>> actividadesPorSala = new TreeMap<>();
                
                    // Agrupar por sala
                    actividadesPorSala = lista.stream()
                        .collect(Collectors.groupingBy(ActividadVO::getSala, TreeMap::new, Collectors.toList()));
                
                    request.setAttribute("agrupadas", true);  
                    request.setAttribute("grupo", actividadesPorSala);
                    break;

                case "nombre":
                    request.setAttribute("agrupadas", false);  
                    break;
                default:
                	request.getSession().setAttribute("error", ordenarPor + " no es una opcion valida de ordenacion");
                    System.out.println("Ordenar por opción no válida: " + ordenarPor);
                    break;
            }
        }
        catch (DAOException e) {
        	System.out.println(e.getMessage());
        	String eAnterior = (String) request.getSession().getAttribute("error");
        	if (eAnterior != null) {
        		request.getSession().setAttribute("error", eAnterior + "\n" + "Error al recuperar la lista de actividades\nIntente más tarde");
        	}
        	else {
        		request.getSession().setAttribute("error", "Error al recuperar la lista de actividades\nIntente más tarde");
        	}
            request.getRequestDispatcher("/html/administrador/home.jsp").forward(request, response);
            return;
        }
        
        // Map para almacenar las imágenes en Base64
        Map<String, String> imagenesBase64 = new HashMap<>();

        // Convertir cada imagen a Base64
        for (ActividadVO actividad : lista) {
            byte[] imagenBytes = actividad.getImagen();
            if (imagenBytes != null && imagenBytes.length > 0) {
                String base64Imagen = Base64.getEncoder().encodeToString(imagenBytes);
                imagenesBase64.put(actividad.getNombre(), base64Imagen); // Usamos el nombre como clave
            }
        }
        request.setAttribute("imagenesBase64", imagenesBase64); // Pasar el mapa de imágenes al JSP
        request.setAttribute("listaActividades", lista);
        request.getRequestDispatcher("/html/administrador/listar-actividades.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
}

package Servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import DAO.ActividadDAO;
import DAO.SalaDAO;
import VO.ActividadVO;
import DAO.UsuarioDAO;
import VO.ClaseVO;
import Exceptions.*;

@MultipartConfig
@WebServlet("/administrador/ModificarActividad")
public class ModificarActividad extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */ 
    public ModificarActividad() {
        super();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    // Método para obtener la extensión del archivo
    private String getFileExtension(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] items = contentDisposition.split(";");
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                String fileName = item.substring(item.indexOf('=') + 2, item.length() - 1);
                return fileName.substring(fileName.lastIndexOf('.') + 1); // Devuelve la extensión
            }
        }
        return ""; // Devuelve una cadena vacía si no se encontró la extensión
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parametros del formulario
        String nombre = request.getParameter("nombre");
        String grupoMuscular = request.getParameter("grupo_muscular");
        String dificultad = request.getParameter("dificultad");
        String material = request.getParameter("material");
        Integer salaAsignada = Integer.parseInt(request.getParameter("sala_asignada"));
        Integer plazasOfertadas = Integer.parseInt(request.getParameter("plazas_ofertadas"));
        String monitor = request.getParameter("monitor");
        String[] diasSeleccionados = request.getParameterValues("dias[]"); // Recibe los días seleccionados
        for (String dia : diasSeleccionados) {
            System.out.println(dia);
        }
               
        // Obtiene la fecha actual
        LocalDate today = LocalDate.now();
        // Inicio del mes que viene para crear las nuevas clases
        LocalDate fechaActual = today.plusMonths(1).withDayOfMonth(1);
        LocalDate fechaLimite = fechaActual.plusMonths(1).minusDays(1);
        // Obtiene el primer día del mes siguiente
        
        ActividadDAO actividadDAO = new ActividadDAO();
        SalaDAO salaDAO = new SalaDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        // Manejo de la imagen subida
        Part filePart = request.getPart("imagen"); // Obtén la parte del archivo
        String imageName = null;
        byte[] imageBytes = null;

        	// Verificar si la imagen fue subida
            if (filePart != null && filePart.getSize() > 0) {
                imageName = nombre + "." + getFileExtension(filePart); // Genera el nombre de la imagen
                
                // Define la ruta donde se guardará la imagen
                String imagesPath = getServletContext().getRealPath("/images");
                File imageFile = new File(imagesPath, imageName);
                imageFile.getParentFile().mkdirs();

                // Guarda la imagen en el servidor
                try (InputStream fileContent = filePart.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(imageFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileContent.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                
                imageBytes = getFileBytes(imageFile);
                
            }
                
            try {
	            // Verificar si la sala esta libre en los horarios dados
	            boolean todoOK =true, salaLibre = true, monitorLibre = true;
	            List<ClaseVO> clases = new ArrayList<>();
	            
	            // Recorremos las fechas desde la fecha actual hasta la fecha límite
	            while (fechaActual.isBefore(fechaLimite) || fechaActual.isEqual(fechaLimite)) {
	                // Recorremos los días seleccionados por el usuario
	                for (String dia : diasSeleccionados) {
	                    String[] horariosInicio = request.getParameterValues(dia + "_inicio[]"); // Horarios de inicio para el día
	                    String[] horariosFin = request.getParameterValues(dia + "_fin[]"); // Horarios de fin para el día
	
	                    // Si hay horarios para este día, procesarlos
	                    if (horariosInicio != null && horariosFin != null) {
	                        for (int i = 0; i < horariosInicio.length; i++) {
	                            // Verificar si los campos no están vacíos
	                            if (horariosInicio[i] == null || horariosInicio[i].isEmpty() || horariosFin[i] == null || horariosFin[i].isEmpty()) {
	                                continue; // Saltar esta iteración si alguno de los campos es vacío
	                            }
	                        	
	                            LocalTime inicio = LocalTime.parse(horariosInicio[i]);
	                            LocalTime fin = LocalTime.parse(horariosFin[i]);
	
	                            System.out.println("Inicio: " + inicio);
	                            System.out.println("Fin: " + fin);
	                            
	                            // Verificamos si la sala y el monitor están libres
	                            salaLibre = !salaDAO.hayClasesEnHorario(salaAsignada, ClaseVO.StringToDayOfWeek(dia), inicio, fin, fechaActual, fechaLimite, nombre);
	                            System.out.println("La sala está libre");
	                            
	                            System.out.println("Parámetros hayClasesEnHorarioMonitor:");
	                            System.out.println("inicio: " + inicio);
	                            System.out.println("fin: " + fin);
	                            System.out.println("correo: " + monitor);
	                            System.out.println("dia: " + dia);
	                            System.out.println("....diaSem: " + ClaseVO.StringToDayOfWeek(dia));
	                            
	                            monitorLibre = !usuarioDAO.hayClasesEnHorarioMonitor(monitor, ClaseVO.StringToDayOfWeek(dia), inicio, fin, fechaActual, fechaLimite, nombre);
	                            System.out.println("El monitor está libre");
	
	                            // Si no están libres, interrumpimos la creación
	                            if (!salaLibre || !monitorLibre) {
	                                todoOK = false;
	                                break;
	                            }
	
	                            // Si está libre, creamos una nueva clase
	                            if (fechaActual.getDayOfWeek() == ClaseVO.StringToDayOfWeek(dia)) {
	                                ClaseVO nuevaClase = new ClaseVO(nombre, fechaActual, fechaActual.getDayOfWeek(), inicio, fin, plazasOfertadas);
	                                clases.add(nuevaClase);
	                                System.out.println("Se añadió una clase");
	                            }
	                        }
	                    }
	                    if (!todoOK) break;
	                }
	
	                if (!todoOK) break;
	
	                // Incrementamos la fecha para el siguiente día
	                fechaActual = fechaActual.plusDays(1);
	            }
	            
	            
	            if (!todoOK) {
	                if (!salaLibre && !monitorLibre) {
	                    request.getSession().setAttribute("error", "La sala y el monitor no estan libres en los horarios especificados.");
	                }
	                else if (!salaLibre) {
	                    request.getSession().setAttribute("error", "La sala no esta libre en los horarios especificados.");
	                }
	                else {  // !monitorLibre
	                    request.getSession().setAttribute("error", "El monitor no esta libre en los horarios especificados.");
	                }
	                request.getRequestDispatcher("/html/administrador/modificar-actividad.jsp"/* Pagina de error */).forward(request, response);
	                return;
	            }
	
	            System.out.println("Clases creadas ");
	            // Modificar la actividad
	            ActividadVO nuevaActividad = new ActividadVO(nombre, grupoMuscular, plazasOfertadas, dificultad, material, salaAsignada, monitor, imageBytes);
	            System.out.println("Actividad creada");
	            
	            // Insertar la actividad y sus clases
	            actividadDAO.updateActividadYClases(nuevaActividad, clases);
	            System.out.println("Actividad y sus clases insertadas");
	            
	            // Redirigir a una pagina de exito o de listado de actividades
	            response.sendRedirect(request.getContextPath() + "/administrador/ListarActividades");

        } catch (ActividadYaExisteException | ClaseYaExisteException e) {
            request.getSession().setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/html/administrador/modificar-actividad.jsp"/* Pagina de error */).forward(request, response);
        } catch (DAOException e) {
            request.getSession().setAttribute("error", "Error en la base de datos: " + e.getMessage());
            request.getRequestDispatcher("/html/administrador/modificar-actividad.jsp"/* Pagina de error */).forward(request, response);
        }
    }
	
    
    // Método para convertir el archivo de imagen en un arreglo de bytes
    private byte[] getFileBytes(File imageFile) throws IOException {
        try (InputStream inputStream = new FileInputStream(imageFile)) {
            byte[] fileBytes = new byte[(int) imageFile.length()];
            inputStream.read(fileBytes);
            return fileBytes;
        }
    }
}

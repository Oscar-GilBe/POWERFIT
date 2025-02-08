package DAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import BD.PoolConnectionManager;
import Exceptions.DAOException;
import Exceptions.ActividadNoEncontradaException;
import Exceptions.ActividadYaExisteException;
import Exceptions.ClaseYaExisteException;
import VO.ActividadVO;
import VO.ClaseVO;

public class ActividadDAO {

    /*
     * Inserta una nueva actividad en la BD
     */
    public void insertActividad(ActividadVO actividad) throws ActividadYaExisteException, DAOException {
        Connection conn = null;

        try {
            conn = PoolConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO actividad (nombre, grupo_muscular, plazas, dificultad, material, sala, monitor, imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            ps.setString(1, actividad.getNombre());
            ps.setString(2, actividad.getGrupo_muscular());
            ps.setInt(3, actividad.getPlazas());
            ps.setString(4, actividad.getDificultad());
            ps.setString(5, actividad.getMaterial());
            ps.setInt(6, actividad.getSala());
            ps.setString(7, actividad.getMonitor());
            ps.setBytes(8, actividad.getImagen());

            ps.executeUpdate();

        } catch (SQLException e) {
            // Verificamos si el error es por violación de clave única en PostgreSQL (SQLSTATE 23505)
            if (e.getSQLState().equals("23505")) {
                throw new ActividadYaExisteException("La actividad con el nombre " + actividad.getNombre() + " ya existe");
            } else {
            	System.out.println(e.getMessage());
                throw new DAOException("Error al insertar actividad en la base de datos", e);
            }
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
    }

    /*
     * Inserta una nueva actividad y sus clases en la BD como una transacción
     */
    public void insertActividadYClases(ActividadVO actividad, List<ClaseVO> clases) throws ActividadYaExisteException, ClaseYaExisteException, DAOException {
        Connection conn = null;

        try {
            // Obtener la conexión y desactivar el auto-commit para manejar la transacción
            conn = PoolConnectionManager.getConnection();
            conn.setAutoCommit(false);  // Desactivar auto-commit para controlar la transacción manualmente

            // Insertar la actividad
            PreparedStatement psActividad = conn.prepareStatement("INSERT INTO actividad (nombre, grupo_muscular, plazas, dificultad, material, sala, monitor, imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            psActividad.setString(1, actividad.getNombre());
            psActividad.setString(2, actividad.getGrupo_muscular());
            psActividad.setInt(3, actividad.getPlazas());
            psActividad.setString(4, actividad.getDificultad());
            psActividad.setString(5, actividad.getMaterial());
            psActividad.setInt(6, actividad.getSala());
            psActividad.setString(7, actividad.getMonitor());
            psActividad.setBytes(8, actividad.getImagen());
            psActividad.executeUpdate();

            // Insertar las clases
            PreparedStatement psClase = conn.prepareStatement("INSERT INTO clase (fecha, dia_semana, inicio, fin, plazas_disponibles, actividad) VALUES (?, ?, ?, ?, ?, ?)");
            for (ClaseVO clase : clases) {
                psClase.setDate(1, Date.valueOf(clase.getFecha()));
                String diaSinTildes = clase.getDia()
                	    .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"))
                	    .toUpperCase()
                	    .replace("Á", "A")
                	    .replace("É", "E")
                	    .replace("Í", "I")
                	    .replace("Ó", "O")
                	    .replace("Ú", "U");

                psClase.setString(2, diaSinTildes);
                psClase.setTime(3, Time.valueOf(clase.getInicio()));
                psClase.setTime(4, Time.valueOf(clase.getFin()));
                psClase.setInt(5, clase.getPlazas());
                psClase.setString(6, clase.getActividad());

                psClase.executeUpdate();
            }

            // Si todo va bien, hacer commit de la transacción
            conn.commit();

        } catch (SQLException e) {
            // Si hay algún error, hacer rollback de los cambios
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                	System.out.println(e.getMessage());
                    throw new DAOException("Error de integridad en la base de datos", ex);
                }
            }

            // Verificar si el error es por una actividad o clase existente
            if (e.getSQLState().equals("23505")) {
                if (e.getMessage().contains("actividad")) {
                    throw new ActividadYaExisteException("La actividad con el nombre " + actividad.getNombre() + " ya existe");
                } else if (e.getMessage().contains("clase")) {
                    throw new ClaseYaExisteException("Una clase de la actividad " + actividad.getNombre() + " ya existe");
                }
            } else {
            	System.out.println(e.getMessage());
                throw new DAOException("Error de integridad en la base de datos", e);
            }
        } finally {
            // Asegurarse de cerrar la conexión y volver a activar el auto-commit
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Volver a activar el auto-commit
                    PoolConnectionManager.releaseConnection(conn); // Liberar la conexión
                } catch (SQLException e) {
                	System.out.println(e.getMessage());
                    throw new DAOException("Error de integridad en la base de datos", e);
                }
            }
        }
    }
    
    /*
     * Actualiza la informacion de una actividad y sus clases en la BD como una transaccion
     */
    public void updateActividadYClases(ActividadVO actividad, List<ClaseVO> clases) throws DAOException, ClaseYaExisteException {
        Connection conn = null;

        try {
            // Obtener la conexión y desactivar el auto-commit para manejar la transacción
            conn = PoolConnectionManager.getConnection();
            conn.setAutoCommit(false);  // Desactivar auto-commit para controlar la transacción manualmente
            
            // Si la imagen es null, obtenemos la imagen actual de la actividad
            if (actividad.getImagen() == null) {
                byte[] imagenActual = getImagenActividad(actividad.getNombre()); // Llamamos a getImagenActividad
                actividad.setImagen(imagenActual); // Asignamos la imagen obtenida a la actividad
            }

            // Actualizar la actividad
            PreparedStatement psActividad = conn.prepareStatement(
                "UPDATE actividad SET grupo_muscular = ?, plazas = ?, dificultad = ?, material = ?, sala = ?, monitor = ?, imagen = ? " +
                "WHERE nombre = ?"
            );
            psActividad.setString(1, actividad.getGrupo_muscular());
            psActividad.setInt(2, actividad.getPlazas());
            psActividad.setString(3, actividad.getDificultad());
            psActividad.setString(4, actividad.getMaterial());
            psActividad.setInt(5, actividad.getSala());
            psActividad.setString(6, actividad.getMonitor());
            psActividad.setBytes(7, actividad.getImagen());
            psActividad.setString(8, actividad.getNombre());
            psActividad.executeUpdate();

            // Si la lista de clases está vacía, terminar la transacción aquí
            if (clases.isEmpty()) {
                conn.commit();
                return;
            }

            // Obtener el mes y el año de la primera clase de la lista
            ClaseVO primeraClase = clases.get(0);
            LocalDate fechaPrimeraClase = primeraClase.getFecha();
            int mes = fechaPrimeraClase.getMonthValue();
            int año = fechaPrimeraClase.getYear();

            // Eliminar las clases existentes en el mes y año de la primera clase
            PreparedStatement psEliminarClases = conn.prepareStatement(
                "DELETE FROM clase WHERE actividad = ? AND EXTRACT(MONTH FROM fecha) = ? AND EXTRACT(YEAR FROM fecha) = ?"
            );
            psEliminarClases.setString(1, actividad.getNombre());
            psEliminarClases.setInt(2, mes);
            psEliminarClases.setInt(3, año);
            psEliminarClases.executeUpdate();

            // Insertar las nuevas clases
            PreparedStatement psClase = conn.prepareStatement(
                "INSERT INTO clase (fecha, dia_semana, inicio, fin, plazas_disponibles, actividad) VALUES (?, ?, ?, ?, ?, ?)"
            );
            for (ClaseVO clase : clases) {
                psClase.setDate(1, Date.valueOf(clase.getFecha()));
                String diaSinTildes = clase.getDia()
                    .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"))
                    .toUpperCase()
                    .replace("Á", "A")
                    .replace("É", "E")
                    .replace("Í", "I")
                    .replace("Ó", "O")
                    .replace("Ú", "U");

                psClase.setString(2, diaSinTildes);
                psClase.setTime(3, Time.valueOf(clase.getInicio()));
                psClase.setTime(4, Time.valueOf(clase.getFin()));
                psClase.setInt(5, clase.getPlazas());
                psClase.setString(6, clase.getActividad());

                psClase.executeUpdate();
            }

            // Si todo va bien, hacer commit de la transacción
            conn.commit();

        } catch (SQLException e) {
            // Si hay algún error, hacer rollback de los cambios
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DAOException("Error de integridad en la base de datos", ex);
                }
            }
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            // Asegurarse de cerrar la conexión y volver a activar el auto-commit
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Volver a activar el auto-commit
                    PoolConnectionManager.releaseConnection(conn); // Liberar la conexión
                } catch (SQLException e) {
                    throw new DAOException("Error de integridad en la base de datos", e);
                }
            }
        }
    }

    /*
     *  Elimina una actividad de la BD
     */
    public void dropActividad(String nombre) throws ActividadNoEncontradaException, DAOException {
        Connection conn = null;
    
        try {
            // Obtener la conexión del pool de conexiones
            conn = PoolConnectionManager.getConnection();
    
            // Preparar la sentencia SQL para eliminar la actividad
            PreparedStatement ps = conn.prepareStatement("DELETE FROM actividad WHERE nombre = ?");
            ps.setString(1, nombre);
    
            // Ejecutar la consulta y verificar si se eliminó alguna fila
            int filasAfectadas = ps.executeUpdate();
    
            // Si no se eliminó ninguna fila, la actividad no existe
            if (filasAfectadas == 0) {
                throw new ActividadNoEncontradaException("No se encontró ninguna actividad con el nombre: " + nombre);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
    }
    
    /*
     *  Devuelve una actividad de la BD dado su nombre
     */
    public ActividadVO getActividad(String nombre) throws ActividadNoEncontradaException, DAOException {
        ActividadVO actividad = null;
        Connection conn = null;

        try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("SELECT * from actividad where nombre = ?");
            ps.setString(1, nombre);
			ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            if (rset.next()) {
                actividad = new ActividadVO(rset.getString("nombre"), rset.getString("grupo_muscular"), rset.getInt("plazas"),
                rset.getString("dificultad"), rset.getString("material"), rset.getInt("sala"), rset.getString("monitor"), rset.getBytes("imagen"));
            } else {
                // Si no hay filas, lanzamos la excepción ReservaNoEncontradaException
                throw new ActividadNoEncontradaException("No se encontró ninguna actividad con nombre: " + nombre);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return actividad;
    }

    /*
     * Devuelve una lista con todas las actividades en la BD
     */
    public List<ActividadVO> listActividades() throws DAOException {
		Connection conn = null;
		List<ActividadVO> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("SELECT * from actividad ORDER BY nombre ASC");
			ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            ActividadVO actividad = null;
            System.out.println("Creando lista de actividades");
            while (rset.next()) {
                actividad = new ActividadVO(rset.getString("nombre"), rset.getString("grupo_muscular"), rset.getInt("plazas"),
                rset.getString("dificultad"), rset.getString("material"), rset.getInt("sala"), rset.getString("monitor"), rset.getBytes("imagen"));
                System.out.println(actividad.toString());
                lista.add(actividad);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return lista;
	}

    /*
     * Devuelve una lista con los horarios de una actividad para una semana con el formato: DIA_SEMANA, inicio(hh:mm)-fin(hh:mm); ...
     */
    public List<String> listHorarios(String actividad) throws DAOException {
        Connection conn = null;
		List<String> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement(
		            "SELECT DISTINCT clase.dia_semana, clase.inicio, clase.fin " +
		            "FROM actividad, clase " +
		            "WHERE actividad.nombre = clase.actividad " +
		            "AND actividad.nombre = ? " +
		            "AND EXTRACT(MONTH FROM clase.fecha) = EXTRACT(MONTH FROM CURRENT_DATE) " +
		            "AND EXTRACT(YEAR FROM clase.fecha) = EXTRACT(YEAR FROM CURRENT_DATE)"
		        );
            ps.setString(1, actividad);
			ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            String horario = "";
            while (rset.next()) {
                horario = rset.getString("dia_semana") + ", " + rset.getTime("inicio") + "-" +  rset.getTime("fin");
                lista.add(horario);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return lista;
    }

    /*
     * Devuelve la puntuacion media de una actividad calculada como la media aritmetica de las puntuaciones
     * de sus reseñas
     */
    public Integer puntuacionMedia(String actividad) throws DAOException{
        Connection conn = null;
        int media = 0;
        try {
            // Obtener la conexión del pool de conexiones
            conn = PoolConnectionManager.getConnection();
    
            // Preparar la sentencia SQL para contar monitores
            PreparedStatement ps = conn.prepareStatement("SELECT AVG(puntuacion) FROM resena WHERE resena.actividad = ?");
            ps.setString(1, actividad);
    
            // Ejecutar la consulta
            ResultSet rset = ps.executeQuery();
    
            // Si no se eliminó ninguna fila, el usuario no existe
            if (rset.next()) {
                media = rset.getInt(1);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error al calcular la puntuacion media en la base de datos.", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        
        return media; 
    }

    /*
     * Devuelve una lista con las clases en la BD de una actividad
     */
    public List<ClaseVO> listClasesActividad(String actividad) throws DAOException {
		Connection conn = null;
		List<ClaseVO> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("SELECT * from Clase WHERE actividad = ?");
            ps.setString(1, actividad);
			ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            ClaseVO clase = null;
            while (rset.next()) {
                LocalDate fecha = rset.getDate("fecha").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                clase = new ClaseVO(rset.getString("actividad"), fecha, fecha.getDayOfWeek(), rset.getTime("inicio").toLocalTime(), rset.getTime("fin").toLocalTime(),
                                    rset.getInt("plazas_disponibles"));
                lista.add(clase);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return lista;
	}

    /*
     * Devuelve una lista con las clases en la BD de una actividad entre una fecha inicio y una fecha fin
     */
    public List<ClaseVO> listClasesActividadPorFecha(String actividad, LocalDate inicio, LocalDate fin) throws DAOException {
		Connection conn = null;
		List<ClaseVO> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("SELECT * from sisinf_p2.clase WHERE actividad = ? " +
                                    "AND clase.fecha <= ? AND clase.fecha >= ? ORDER BY clase.fecha ASC");
            ps.setString(1, actividad);
            ps.setDate(2, Date.valueOf(fin));
            ps.setDate(3, Date.valueOf(inicio));

			ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            ClaseVO clase = null;
            while (rset.next()) {
                LocalDate fecha = rset.getDate("fecha").toLocalDate();
                clase = new ClaseVO(rset.getString("actividad"), fecha, fecha.getDayOfWeek(), rset.getTime("inicio").toLocalTime(), rset.getTime("fin").toLocalTime(),
                                    rset.getInt("plazas_disponibles"));
                lista.add(clase);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos. Actividad: " + actividad + ", Inicio: " + inicio + ", Fin: " + fin, e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return lista;
	}
    
  /*
   * Devuelve el numero de actividades existentes en la BD
   */
    public Integer countActividades() throws DAOException {
        Connection conn = null;
        int count = 0;
        try {
            // Obtener la conexión del pool de conexiones
            conn = PoolConnectionManager.getConnection();

            // Preparar la sentencia SQL para contar monitores
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM actividad");

            // Ejecutar la consulta
            ResultSet rset = ps.executeQuery();

            // Numero de actividades
            if (rset.next()) {
                count = rset.getInt(1);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error al contar las actividades en la base de datos.", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        
        return count; 
    }
    
    /*
     * Devuelve la imagen de la actividad dado su nombre
     */
    public byte[] getImagenActividad(String nombre) throws ActividadNoEncontradaException, DAOException {
        byte[] imagen = null;
        Connection conn = null;

        try {
            // Abrimos la conexión
            conn = PoolConnectionManager.getConnection();
            
            // Preparar la sentencia SQL para obtener la imagen de la actividad
            PreparedStatement ps = conn.prepareStatement("SELECT imagen FROM actividad WHERE nombre = ?");
            ps.setString(1, nombre);

            // Ejecutar la consulta
            ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            if (rset.next()) {
                imagen = rset.getBytes("imagen");  // Obtener los bytes de la imagen
            } else {
                // Si no se encuentra la actividad, lanzamos una excepción
                throw new ActividadNoEncontradaException("No se encontró ninguna actividad con nombre: " + nombre);
            }
        } catch (SQLException e) {
            // Si ocurre un error en la consulta SQL, lo propagamos
            System.out.println(e.getMessage());
            throw new DAOException("Error al obtener la imagen de la actividad en la base de datos", e);
        } finally {
            // Liberar la conexión
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }

        return imagen;
    }
    
    /* MÉTODOS COMENTADOS PORQUE NO SE USAN, PUEDEN SER ÚTILES EN UN FUTURO
    // Actualiza la imagen de una actividad
    public void updateImagen(String actividadNombre, byte[] nuevaImagen) throws DAOException {
        Connection conn = null;

        try {
            conn = PoolConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE actividad SET imagen = ? WHERE nombre = ?"
            );
            ps.setBytes(1, nuevaImagen);  // Actualiza los bytes de la imagen
            ps.setString(2, actividadNombre);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new DAOException("No se encontró la actividad: " + actividadNombre);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar la imagen en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
    }

    // Elimina la imagen de una actividad
    public void deleteImagen(String actividadNombre) throws DAOException {
        Connection conn = null;

        try {
            conn = PoolConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE actividad SET imagen = NULL WHERE nombre = ?"
            );
            ps.setString(1, actividadNombre);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new DAOException("No se encontró ninguna imagen para la actividad: " + actividadNombre);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al eliminar la imagen de la actividad", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
    }*/

}
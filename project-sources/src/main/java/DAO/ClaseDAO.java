package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.sql.Date;

import BD.PoolConnectionManager;
import Exceptions.DAOException;
import Exceptions.ClaseNoEncontradaException;
import Exceptions.ClaseYaExisteException;
import VO.ClaseVO;

public class ClaseDAO {

    /*
     * Inserta una nueva clase en la BD
     */
    public void insertClase(ClaseVO Clase) throws ClaseYaExisteException, DAOException {
        Connection conn = null;

        try {
            conn = PoolConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Clase (fecha, dia_semana, inicio, fin, plazas_disponibles, actividad) VALUES (?, ?, ?, ?, ?, ?)");

            ps.setDate(1, Date.valueOf(Clase.getFecha()));
            ps.setString(2, Clase.getDia().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")).toLowerCase());
            ps.setTime(3, Time.valueOf(Clase.getInicio()));
            ps.setTime(4, Time.valueOf(Clase.getFin()));
            ps.setInt(5, Clase.getPlazas());
            ps.setString(6, Clase.getActividad());

            ps.executeUpdate();

        } catch (SQLException e) {
            // Verificamos si el error es por violación de clave única en PostgreSQL (SQLSTATE 23505)
            if (e.getSQLState().equals("23505")) {
                throw new ClaseYaExisteException("La Clase de la actividad " + Clase.getActividad() + " en el dia " + Clase.getFecha()
                    + " de " + Clase.getInicio() + " a " + Clase.getFin() + " ya existe");
            } else {
            	System.out.println(e.getMessage());
                throw new DAOException("Error de integridad en la base de datos", e);
            }
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
    }

    /*
     * Elimina una clase de la BD
     */
    public void dropClase(String actividad, LocalDate fecha, LocalTime inicio) throws ClaseNoEncontradaException, DAOException {
        Connection conn = null;
    
        try {
            // Obtener la conexión del pool de conexiones
            conn = PoolConnectionManager.getConnection();
    
            // Preparar la sentencia SQL para eliminar la Clase
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Clase WHERE actividad = ? AND fecha = ? AND inicio = ?");
            ps.setString(1, actividad);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setTime(3, Time.valueOf(inicio));
    
            // Ejecutar la consulta y verificar si se eliminó alguna fila
            int filasAfectadas = ps.executeUpdate();
    
            // Si no se eliminó ninguna fila, la Clase no existe
            if (filasAfectadas == 0) {
                throw new ClaseNoEncontradaException("No se encontró ninguna Clase de la actividad " + actividad + " en el dia " + fecha
                    + " a las " + inicio);
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
     * Actualiza una clase de la BD
     */
    public void updateClase(ClaseVO clase) throws ClaseNoEncontradaException, DAOException {
        Connection conn = null;
    
        try {
            // Obtener la conexión del pool de conexiones
            conn = PoolConnectionManager.getConnection();
            
            // Preparar la sentencia SQL para actualizar los campos de la clase
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE Clase SET dia_semana = ?, inicio = ?, fin = ?, plazas_disponibles = ?, actividad = ? WHERE fecha = ? AND actividad = ? AND inicio = ?"
            );
            
            // Asignamos los valores a los parámetros de la consulta
            ps.setString(1, clase.getDia().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")).toLowerCase());
            ps.setTime(2, Time.valueOf(clase.getInicio()));
            ps.setTime(3, Time.valueOf(clase.getFin()));
            ps.setInt(4, clase.getPlazas());
            ps.setString(5, clase.getActividad());
    
            // Condiciones para encontrar la clase a actualizar
            ps.setDate(6, Date.valueOf(clase.getFecha()));
            ps.setString(7, clase.getActividad());
            ps.setTime(8, Time.valueOf(clase.getInicio()));
    
            // Ejecutar la actualización y verificar si se afectó alguna fila
            int filasAfectadas = ps.executeUpdate();
    
            // Si no se actualizó ninguna fila, la clase no existe
            if (filasAfectadas == 0) {
                throw new ClaseNoEncontradaException("No se encontró ninguna Clase de la actividad " + clase.getActividad() + 
                    " en el dia " + clase.getFecha() + " a las " + clase.getInicio());
            }
            
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            // Liberar la conexión
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
    }

    /*
     * Devuelve una clase de la BD
     */
    public ClaseVO getClase(String actividad, LocalDate fecha, LocalTime inicio) throws ClaseNoEncontradaException, DAOException {
        ClaseVO clase = null;
        Connection conn = null;

        try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("SELECT * from Clase WHERE actividad = ? AND fecha = ? AND inicio = ?");
            ps.setString(1, actividad);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setTime(3, Time.valueOf(inicio));
			ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            if (rset.next()) {
                clase = new ClaseVO(rset.getString("actividad"), fecha, fecha.getDayOfWeek(),
                        rset.getTime("inicio").toLocalTime(), rset.getTime("fin").toLocalTime(), rset.getInt("plazas_disponibles"));
            } else {
                // Si no hay filas, lanzamos la excepción ReservaNoEncontradaException
                throw new ClaseNoEncontradaException("No se encontró ninguna Clase de la actividad " + actividad + " en el dia " + fecha
                    + " a las " + inicio);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return clase;
    }
    
    /*
     * Devuelve verdad si y solo si una clase tiene plazas disponibles (plazas disponibles != 0)
     */
    public boolean hayPlazasDisponibles(String actividad, LocalDate fecha, LocalTime inicio) throws DAOException {
        Connection conn = null;
        boolean hayPlazas = false;  // Inicializamos hayPlazas a false por defecto
    
        try {
            // Abrimos la conexión e inicializamos los parámetros 
            conn = PoolConnectionManager.getConnection(); 
            PreparedStatement ps = conn.prepareStatement("SELECT plazas_disponibles FROM Clase WHERE actividad = ? AND fecha = ? AND inicio = ?");
            ps.setString(1, actividad);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setTime(3, Time.valueOf(inicio));
            ResultSet rset = ps.executeQuery();
    
            // Si la consulta devuelve un resultado, verificamos el número de plazas
            if (rset.next()) {
                int plazasDisponibles = rset.getInt("plazas_disponibles");
                hayPlazas = plazasDisponibles > 0;  // true si hay plazas, false si no
            }
    
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            // Liberamos la conexión independientemente del resultado
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
    
        return hayPlazas;
    }
    
}

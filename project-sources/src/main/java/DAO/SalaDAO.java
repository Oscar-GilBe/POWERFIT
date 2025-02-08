package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import BD.PoolConnectionManager;
import Exceptions.DAOException;
import Exceptions.SalaNoEncontradaException;
import Exceptions.SalaYaExisteException;
import VO.SalaVO;
import VO.ClaseVO;
import VO.ActividadVO;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class SalaDAO {

    /*
     * Inserta una nueva sala en la BD
     */
    public void insertSala(SalaVO sala) throws SalaYaExisteException, DAOException {
        Connection conn = null;

        try {
            conn = PoolConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO sala (numero) VALUES (?)");

            ps.setInt(1, sala.getNumero());

            ps.executeUpdate();

        } catch (SQLException e) {
            // Verificamos si el error es por violación de clave única en PostgreSQL (SQLSTATE 23505)
            if (e.getSQLState().equals("23505")) {
                throw new SalaYaExisteException("La sala con el número " + sala.getNumero() + " ya existe");
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
     * Elimina una sala de la BD
     */
    public void dropSala(Integer numSala) throws SalaNoEncontradaException, DAOException {
        Connection conn = null;
    
        try {
            // Obtener la conexión del pool de conexiones
            conn = PoolConnectionManager.getConnection();
    
            // Preparar la sentencia SQL para eliminar la sala
            PreparedStatement ps = conn.prepareStatement("DELETE FROM sala WHERE numero = ?");
            ps.setInt(1, numSala);
    
            // Ejecutar la consulta y verificar si se eliminó alguna fila
            int filasAfectadas = ps.executeUpdate();
    
            // Si no se eliminó ninguna fila, la sala no existe
            if (filasAfectadas == 0) {
                throw new SalaNoEncontradaException("No se encontró ninguna sala con el número: " + numSala);
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
     * Devuelve la sala de la BD con ese numero
     */
    public SalaVO getSala(Integer numSala) throws SalaNoEncontradaException, DAOException {
		Connection conn = null;
		SalaVO sala = null;

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("SELECT * from sala where numero = ?");
			ps.setInt(1, numSala);
			ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            if (rset.next()) {
                sala = new SalaVO(rset.getInt("numero"));
            } else {
                // Si no hay filas, lanzamos la excepción SalaNoEncontradaException
                throw new SalaNoEncontradaException("No se encontró la sala con el número :" + numSala);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return sala;
	}

    /*
     * Devuelve una lista con todas las salas de la BD    
     */
    public List<SalaVO> listSalas() throws DAOException {
		Connection conn = null;
		List<SalaVO> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("SELECT * from sala");
			ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            SalaVO sala = null;
            while (rset.next()) {
                sala = new SalaVO(rset.getInt("numero"));
                lista.add(sala);
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
     * Devuelve una lista con las actividades que tiene asociadas una sala en la BD    
     */
    public List<ActividadVO> listActividadesSala(Integer numSala) throws DAOException {
		Connection conn = null;
		List<ActividadVO> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("SELECT * from actividad where actividad.sala = ?");
			ps.setInt(1, numSala);
			ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            ActividadVO actividad = null;
            while (rset.next()) {
                actividad = new ActividadVO(rset.getString("nombre"), rset.getString("grupo_muscular"), rset.getInt("plazas"),
                        rset.getString("dificultad"), rset.getString("material"), rset.getInt("sala"), rset.getString("monitor"), rset.getBytes("imagen"));
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
     * Devuelve verdad si y solo si la sala tiene asignada una actividad distinta de la dada, en el horario inicio-fin entre los dias f_ini y f_fin
     */
    public boolean hayClasesEnHorario(int sala, DayOfWeek dia, LocalTime inicio, LocalTime fin, LocalDate f_ini, LocalDate f_fin, String actividad) throws DAOException {
        Connection conn = null;
        boolean resultado = false;

        try {
            // Abrimos la conexión e inicializamos los parámetros
            conn = PoolConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT c.actividad FROM sisinf_p2.clase c " +
                "JOIN sisinf_p2.actividad a ON c.actividad = a.nombre " +
                "WHERE a.sala = ? AND c.dia_semana = ? " +
                "AND (" +
                "c.inicio = ? OR c.fin = ? OR " +
                "(c.inicio > ? AND c.fin < ?) OR " +
                "(c.inicio < ? AND c.fin > ?) OR " +
                "(c.inicio > ? AND c.fin > ? AND c.inicio < ?) OR " +
                "(c.inicio < ? AND c.fin < ? AND c.fin > ?)" +
                ") " +
                "AND c.fecha BETWEEN ? AND ? " +
                "AND NOT (a.nombre = ?) "
            );

            ps.setInt(1, sala);
            ps.setString(2, ClaseVO.DayOfWeekToString(dia));
            ps.setObject(3, inicio);
            ps.setObject(4, fin);
            ps.setObject(5, inicio);
            ps.setObject(6, fin);
            ps.setObject(7, inicio);
            ps.setObject(8, fin);
            ps.setObject(9, inicio);
            ps.setObject(10, fin);
            ps.setObject(11, fin);
            ps.setObject(12, inicio);
            ps.setObject(13, fin);
            ps.setObject(14, inicio);
            ps.setObject(15, f_ini);
            ps.setObject(16, f_fin);
            ps.setObject(17, actividad);

            System.out.println("Consulta SQL: " + ps.toString());
            ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            resultado = rset.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return resultado;
    }

}

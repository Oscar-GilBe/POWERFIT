package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import BD.PoolConnectionManager;
import Exceptions.DAOException;
import Exceptions.ResenaNoEncontradaException;
import Exceptions.ResenaYaExisteException;
import VO.ResenaVO;

public class ResenaDAO {

    /*
     * Inserta una nueva resena en la BD
     */
    public void insertResena(ResenaVO resena) throws ResenaYaExisteException, DAOException {
        Connection conn = null;

        try {
            conn = PoolConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO resena (puntuacion, texto, cliente, actividad) VALUES (?, ?, ?, ?)");
            ps.setInt(1, resena.getPuntuacion());
            ps.setString(2, resena.getTexto());
            ps.setString(3, resena.getCliente());
            ps.setString(4, resena.getActividad());

            ps.executeUpdate();

        } catch (SQLException e) {
            // Verificamos si el error es por violación de clave única en PostgreSQL (SQLSTATE 23505)
            if (e.getSQLState().equals("23505")) {
                throw new ResenaYaExisteException("Ya existe una reseña del cliente " + resena.getCliente() + " para la actividad " + resena.getActividad());
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
      * Actualiza una resena en la BD
      */
    public void updateResena(ResenaVO resena) throws ResenaYaExisteException, DAOException {
        Connection conn = null;

        try {
            conn = PoolConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE resena SET puntuacion = ?, texto = ?, cliente = ?, actividad = ? WHERE cliente = ? AND actividad = ?");
            ps.setInt(1, resena.getPuntuacion());
            ps.setString(2, resena.getTexto());
            ps.setString(3, resena.getCliente());
            ps.setString(4, resena.getActividad());
            ps.setString(5, resena.getCliente());
            ps.setString(6, resena.getActividad());

            int filasAfectadas= ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new ResenaNoEncontradaException("No se encontró ninguna resena del cliente " + resena.getCliente()
                             + " para la actividad " + resena.getActividad());
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
     * Elimina una resena de la BD
     */
    public void dropResena(String cliente, String actividad) throws ResenaNoEncontradaException, DAOException {
        Connection conn = null;
    
        try {
            // Obtener la conexión del pool de conexiones
            conn = PoolConnectionManager.getConnection();
    
            // Preparar la sentencia SQL para eliminar la resena
            PreparedStatement ps = conn.prepareStatement("DELETE FROM resena WHERE cliente = ? AND actividad = ?");
            ps.setString(1, cliente);
            ps.setString(2, actividad);
    
            // Ejecutar la consulta y verificar si se eliminó alguna fila
            int filasAfectadas = ps.executeUpdate();
    
            // Si no se eliminó ninguna fila, la resena no existe
            if (filasAfectadas == 0) {
                throw new ResenaNoEncontradaException("No se encontró ninguna resena del cliente " + cliente + " para la actividad " + actividad);
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
     * Devuelve una resena de la base de datos dado el cliente que la puso y la actividad a la que se refiere  
     */
    public ResenaVO getResena(String cliente, String actividad) throws ResenaNoEncontradaException, DAOException {
		Connection conn = null;
		ResenaVO resena = null;

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("SELECT * from resena where cliente = ? AND actividad = ?");
			ps.setString(1, cliente);
            ps.setString(2, actividad);
			ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            if (rset.next()) {
                resena = new ResenaVO(rset.getInt("puntuacion"), rset.getString("texto"), rset.getString("cliente"), rset.getString("actividad"));
            } else {
                // Si no hay filas, lanzamos la excepción ResenaNoEncontradaException
                throw new ResenaNoEncontradaException("No se encontró ninguna reseña del cliente " + cliente + " para la actividad " + actividad);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return resena;
	}

    /*
     * Devuelve una lista con todas las resenas de un cliente
     */
    public List<ResenaVO> listResenasPorCliente(String cliente) throws DAOException {
		Connection conn = null;
		List<ResenaVO> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM resena WHERE resena.cliente = ? order by resena.actividad");
            ps.setString(1, cliente);
            
			ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            ResenaVO resena = null;
            while (rset.next()) {
                resena = new ResenaVO(rset.getInt("puntuacion"), rset.getString("texto"), 
                         rset.getString("cliente"), rset.getString("actividad"));
                lista.add(resena);
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
     * Devuelve una lista con todas las resenas de una actividad    
     */
    public List<ResenaVO> listResenasPorActividad(String actividad) throws DAOException {
		Connection conn = null;
		List<ResenaVO> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM resena WHERE resena.actividad = ? order by resena.cliente");
            ps.setString(1, actividad);
            
			ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            ResenaVO resena = null;
            while (rset.next()) {
                resena = new ResenaVO(rset.getInt("puntuacion"), rset.getString("texto"), 
                         rset.getString("cliente"), rset.getString("actividad"));
                lista.add(resena);
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
}

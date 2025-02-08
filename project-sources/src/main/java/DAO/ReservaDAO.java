package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime; 
import java.sql.Date;

import BD.PoolConnectionManager;
import VO.ReservaVO;
import VO.ClaseVO;
import Exceptions.*;

public class ReservaDAO {
    /*
     * Inserta una reserva y actualiza las plazas de la clase en la BD como una transacción
     */
    public void insertReserva(ReservaVO reserva) throws ReservaYaExisteException, DAOException {
        Connection conn = null;
        ClaseDAO claseDAO = new ClaseDAO();

        try {
            // Obtener la conexión y desactivar el auto-commit para controlar la transacción manualmente
            conn = PoolConnectionManager.getConnection();
            conn.setAutoCommit(false);  // Iniciar transacción
            
            // Obtener la clase correspondiente
            ClaseVO clase = claseDAO.getClase(reserva.getActividad(), reserva.getFecha(), reserva.getHora());

            // Verificar si hay plazas disponibles
            if (clase.getPlazas() > 0) {
                // Reducir el número de plazas y actualizar la clase en la BD
                clase.setPlazas(clase.getPlazas() - 1);
                
                // Actualizar la clase en la BD (actualización de plazas)
                PreparedStatement psUpdateClase = conn.prepareStatement(
                    "UPDATE Clase SET plazas_disponibles = ? WHERE actividad = ? AND fecha = ? AND inicio = ?"
                );
                psUpdateClase.setInt(1, clase.getPlazas());
                psUpdateClase.setString(2, clase.getActividad());
                psUpdateClase.setDate(3, Date.valueOf(clase.getFecha()));
                psUpdateClase.setTime(4, Time.valueOf(clase.getInicio()));
                psUpdateClase.executeUpdate();

                // Insertar la nueva reserva en la BD
                PreparedStatement psInsertReserva = conn.prepareStatement(
                    "INSERT INTO Reserva (cliente, actividad, fecha, hora) VALUES (?, ?, ?, ?)"
                );
                psInsertReserva.setString(1, reserva.getCliente());
                psInsertReserva.setString(2, reserva.getActividad());
                psInsertReserva.setDate(3, Date.valueOf(reserva.getFecha()));
                psInsertReserva.setTime(4, Time.valueOf(reserva.getHora()));
                psInsertReserva.executeUpdate();

                // Si todo fue exitoso, confirmar la transacción
                conn.commit();

            } else {
                // Si no hay plazas disponibles, lanzar excepción
                throw new SinPlazasDisponiblesException("No hay plazas disponibles para la actividad " + reserva.getActividad() + 
                    " en la fecha " + reserva.getFecha() + " a la hora " + reserva.getHora());
            }

        } catch (SQLException e) {
            // Si ocurre un error, deshacer los cambios en la transacción
            if (conn != null) {
                try {
                    conn.rollback();  // Deshacer la transacción en caso de error
                } catch (SQLException rollbackEx) {
                	System.out.println(rollbackEx.getMessage());
                    throw new DAOException("Error al hacer rollback de la transacción", rollbackEx);
                }
            }

            // Verificamos si el error es por violación de clave única (reserva duplicada)
            if (e.getSQLState().equals("23505")) {
                throw new ReservaYaExisteException("La reserva del cliente " + reserva.getCliente() + 
                    " para la actividad " + reserva.getActividad() + " en el dia " + reserva.getFecha() + 
                    " a la hora " + reserva.getHora() + " ya existe");
            } else {
            	System.out.println(e.getMessage());
                throw new DAOException("Error de integridad en la base de datos", e);
            }
            
        } catch (ClaseNoEncontradaException e) {
            // Si la clase no se encuentra, lanzar la excepción correspondiente
            throw e;

        } finally {
            // Restaurar el estado de auto-commit y liberar la conexión
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Restaurar el modo de auto-commit
                } catch (SQLException e) {
                	System.out.println(e.getMessage());
                    throw new DAOException("Error al restaurar el auto-commit", e);
                }
                PoolConnectionManager.releaseConnection(conn);
            }
        }
    }

    /*
     * Elimina una reserva y aumenta el número de plazas de la clase en la BD como una transacción
     */
    public void dropReserva(String cliente, String actividad, LocalDate fecha, LocalTime inicio) throws ReservaNoEncontradaException, DAOException {
        Connection conn = null;
        ClaseDAO claseDAO = new ClaseDAO();

        try {
            // Obtener la conexión y desactivar el auto-commit para manejar la transacción manualmente
            conn = PoolConnectionManager.getConnection();
            conn.setAutoCommit(false);  // Iniciar transacción
            
            // Obtener la clase correspondiente
            ClaseVO clase = claseDAO.getClase(actividad, fecha, inicio);

            // Construcción de la consulta en forma de cadena para depuración
            String queryDebug = String.format(
                "DELETE FROM sisinf_p2.RESERVA WHERE cliente = '%s' AND actividad = '%s' AND fecha = '%s' AND hora = '%s'",
                cliente,
                actividad,
                Date.valueOf(fecha),
                Time.valueOf(inicio)
            );
            System.out.println("Query completa: " + queryDebug);
            
            // Preparar la sentencia SQL para eliminar la reserva
            PreparedStatement psDeleteReserva = conn.prepareStatement(
                "DELETE FROM sisinf_p2.RESERVA WHERE cliente = ? AND actividad = ? AND fecha = ? AND hora = ?"
            );
            psDeleteReserva.setString(1, cliente);
            psDeleteReserva.setString(2, actividad);
            psDeleteReserva.setDate(3, Date.valueOf(fecha));
            psDeleteReserva.setTime(4, Time.valueOf(inicio));

            // Ejecutar la eliminación de la reserva y verificar si se eliminó alguna fila
            int filasAfectadas = psDeleteReserva.executeUpdate();

            if (filasAfectadas == 0) {
                // Si no se eliminó ninguna fila, la reserva no existe
                throw new ReservaNoEncontradaException("No se encontró ninguna reserva del cliente " + cliente + 
                    " para la actividad " + actividad + " el dia " + fecha + " a la hora " + inicio);
            }

            // Incrementar el número de plazas disponibles de la clase y actualizarla en la BD
            clase.setPlazas(clase.getPlazas() + 1);

            // Actualizar la clase en la BD (incremento de plazas)
            PreparedStatement psUpdateClase = conn.prepareStatement(
                "UPDATE Clase SET plazas_disponibles = ? WHERE actividad = ? AND fecha = ? AND inicio = ?"
            );
            psUpdateClase.setInt(1, clase.getPlazas());
            psUpdateClase.setString(2, clase.getActividad());
            psUpdateClase.setDate(3, Date.valueOf(clase.getFecha()));
            psUpdateClase.setTime(4, Time.valueOf(clase.getInicio()));
            psUpdateClase.executeUpdate();

            // Si todo fue exitoso, confirmar la transacción
            conn.commit();

        } catch (SQLException e) {
            // Si ocurre un error, deshacer los cambios en la transacción
            if (conn != null) {
                try {
                    conn.rollback();  // Deshacer la transacción en caso de error
                } catch (SQLException rollbackEx) {
                	System.out.println(rollbackEx.getMessage());
                    throw new DAOException("Error al hacer rollback de la transacción", rollbackEx);
                }
            }
            System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);

        } catch (ClaseNoEncontradaException e) {
            // Si no se encuentra la clase, lanzar excepción correspondiente
            throw new DAOException("No se encontró la clase para la actividad " + actividad + " en la fecha " + fecha + " a la hora " + inicio, e);

        } finally {
            // Restaurar el estado de auto-commit y liberar la conexión
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Restaurar el auto-commit
                } catch (SQLException e) {
                	System.out.println(e.getMessage());
                    throw new DAOException("Error al restaurar el auto-commit", e);
                }
                PoolConnectionManager.releaseConnection(conn);
            }
        }
    }
    
    /*
     * Devuelve la reserva en la BD de un cliente para una clase de una actividad  
     */
    public ReservaVO getReserva(String cliente, String actividad, LocalDate fecha, LocalTime hora) throws ReservaNoEncontradaException, DAOException {
		Connection conn = null;
		ReservaVO Reserva = null;

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("SELECT * from Reserva where cliente = ? AND actividad = ? AND fecha = ? AND hora = ?");
            ps.setString(1, cliente);
            ps.setString(2, actividad);
            ps.setDate(3, Date.valueOf(fecha));
            ps.setTime(4, Time.valueOf(hora));
			ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            if (rset.next()) {
                Reserva = new ReservaVO(rset.getString("cliente"), rset.getString("actividad"), rset.getDate("fecha").toLocalDate(), rset.getTime("hora").toLocalTime());
            } else {
                // Si no hay filas, lanzamos la excepción ReservaNoEncontradaException
                throw new ReservaNoEncontradaException("No se encontró ninguna reserva del cliente " + cliente + " para la actividad " 
                + actividad + " el dia " + fecha + " a la hora " + hora);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return Reserva;
	}

}

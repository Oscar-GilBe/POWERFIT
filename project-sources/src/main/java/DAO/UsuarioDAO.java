package DAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.time.ZoneId;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import BD.PoolConnectionManager;
import Exceptions.DAOException;
import Exceptions.UsuarioNoEncontradoException;
import Exceptions.UsuarioYaExisteException;
import VO.UsuarioVO;
import VO.ClaseVO;
import VO.TipoUsuario;

public class UsuarioDAO {

    /*
     * Inserta un nuevo usuario en la BD
     */
    public void insertUsuario(UsuarioVO usuario) throws UsuarioYaExisteException, DAOException {
        Connection conn = null;
        try {
            conn = PoolConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO usuario (correo, nombre, apellidos, contrasena) VALUES (?, ?, ?, ?)");
            
            ps.setString(1, usuario.getCorreo());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellidos());
            ps.setString(4, usuario.getContrasena());

            ps.executeUpdate();

        } catch (SQLException e) {
            // Verificamos si el error es por violación de clave única en PostgreSQL (SQLSTATE 23505)
            if (e.getSQLState().equals("23505")) {
                throw new UsuarioYaExisteException("El usuario con el correo " + usuario.getCorreo() + " ya existe");
            } else {
            	System.out.println(e.getMessage());
                throw new DAOException("Error de integridad en la base de datos", e);
            }
        } 

        switch (usuario.getTipo()) {
            case CLIENTE:
                try {
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO cliente (correo) VALUES (?)");
                    ps.setString(1, usuario.getCorreo());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    // Verificamos si el error es por violación de clave única en PostgreSQL (SQLSTATE 23505)
                    if (e.getSQLState().equals("23505")) {
                        throw new UsuarioYaExisteException("El cliente con el correo " + usuario.getCorreo() + " ya existe");
                    } else {
                    	System.out.println(e.getMessage());
                        throw new DAOException(e.getMessage(), e);
                    }
                }
                finally {
                    if (conn != null) {
                        PoolConnectionManager.releaseConnection(conn);
                    }
                } 
                break;
            case MONITOR:
                try {
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO monitor (correo) VALUES (?)");
                    ps.setString(1, usuario.getCorreo());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    // Verificamos si el error es por violación de clave única en PostgreSQL (SQLSTATE 23505)
                    if (e.getSQLState().equals("23505")) {
                        throw new UsuarioYaExisteException("El monitor con el correo " + usuario.getCorreo() + " ya existe");
                    } else {
                    	System.out.println(e.getMessage());
                        throw new DAOException("Error de integridad en la base de datos", e);
                    }
                } 
                finally {
                    if (conn != null) {
                        PoolConnectionManager.releaseConnection(conn);
                    }
                }
                break;
            case ADMINISTRADOR:
                try {
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO administrador (correo) VALUES (?)");
                    ps.setString(1, usuario.getCorreo());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    // Verificamos si el error es por violación de clave única en PostgreSQL (SQLSTATE 23505)
                    if (e.getSQLState().equals("23505")) {
                        throw new UsuarioYaExisteException("El administrador con el correo " + usuario.getCorreo() + " ya existe");
                    } else {
                    	System.out.println(e.getMessage());
                        throw new DAOException("Error de integridad en la base de datos", e);
                    }
                } 
                finally {
                    if (conn != null) {
                        PoolConnectionManager.releaseConnection(conn);
                    }
                }
        }
    }

    /*
     * Elimina un usuario de la BD
     */
    public void dropUsuario(String correo) throws UsuarioNoEncontradoException, DAOException {
        Connection conn = null;
    
        try {
            // Obtener la conexión del pool de conexiones
            conn = PoolConnectionManager.getConnection();
    
            // Preparar la sentencia SQL para eliminar el usuario
            PreparedStatement ps = conn.prepareStatement("DELETE FROM usuario WHERE correo = ?");
            ps.setString(1, correo);
    
            // Ejecutar la consulta y verificar si se eliminó alguna fila
            int filasAfectadas = ps.executeUpdate();
    
            // Si no se eliminó ninguna fila, el usuario no existe
            if (filasAfectadas == 0) {
                throw new UsuarioNoEncontradoException("No se encontró ningún usuario con el correo: " + correo);
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
     * Devuelve un usuario de la BD dado su correo    
     */
    public UsuarioVO getUsuario(String correo) throws UsuarioNoEncontradoException, DAOException {
		Connection conn = null;
		UsuarioVO usuario = null;

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps1 = conn.prepareStatement("SELECT * from usuario where correo = ?");
			ps1.setString(1, correo);
			ResultSet rset1 = ps1.executeQuery();

            // Verificamos si hay resultados
            if (rset1.next()) {
                PreparedStatement ps2 = conn.prepareStatement("SELECT * from cliente where correo = ?");
                ps2.setString(1, correo);
                ResultSet rset2 = ps2.executeQuery();
                if (rset2.next()) { // El usuario es un cliente
                    usuario = new UsuarioVO(rset1.getString("correo"), rset1.getString("nombre"),
                    rset1.getString("apellidos"), rset1.getString("contrasena"), TipoUsuario.CLIENTE);
                } else {
                    ps2 = conn.prepareStatement("SELECT * from monitor where correo = ?");
                    ps2.setString(1, correo);
                    rset2 = ps2.executeQuery();
                    if (rset2.next()) { // El usuario es un monitor
                        usuario = new UsuarioVO(rset1.getString("correo"), rset1.getString("nombre"),
                        rset1.getString("apellidos"), rset1.getString("contrasena"), TipoUsuario.MONITOR);
                    } else {    // El usuario es un administrador
                        usuario = new UsuarioVO(rset1.getString("correo"), rset1.getString("nombre"),
                        rset1.getString("apellidos"), rset1.getString("contrasena"), TipoUsuario.ADMINISTRADOR);
                    }
                }                
            } else {
                // Si no hay filas, lanzamos la excepción UsuarioNoEncontradoException
                throw new UsuarioNoEncontradoException("No se encontró el usuario con el correo: " + correo);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return usuario;
	}

    /*
     * Devuelve una lista con los usuarios de tipo monitor de la BD    
     */
    public List<UsuarioVO> listMonitores() throws DAOException {
        Connection conn = null;
        List<UsuarioVO> lista = new ArrayList<>();

        try {
            // Abrimos la conexión e inicializamos los parámetros 
            conn = PoolConnectionManager.getConnection(); 
            PreparedStatement ps = conn.prepareStatement("SELECT usuario.correo, usuario.nombre, usuario.apellidos," +
                                    " usuario.contrasena FROM usuario, monitor WHERE usuario.correo = monitor.correo");
            ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            UsuarioVO usuario = null;
            while (rset.next()) {
                usuario = new UsuarioVO(rset.getString("correo"), rset.getString("nombre"),
                rset.getString("apellidos"), rset.getString("contrasena"), TipoUsuario.MONITOR);
                lista.add(usuario);
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
     * Actualiza la informacion de un usuario en la BD    
     */
    public void updateUsuario(UsuarioVO usuario) throws UsuarioNoEncontradoException, DAOException{
        Connection conn = null;

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE usuario SET nombre = ?, apellidos = ?, contrasena = ? WHERE correo = ?");
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getCorreo());

            int filasAfectadas= ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new UsuarioNoEncontradoException("No se encontró ningún usuario con el correo: " + usuario.getCorreo());
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
     * Devuelve una lista con las clases asignadas a un monitor
     */
    public List<ClaseVO> listClasesMonitor(String correo) throws DAOException {
        Connection conn = null;
        List<ClaseVO> lista = new ArrayList<>();

        try {
            // Abrimos la conexión e inicializamos los parámetros 
            conn = PoolConnectionManager.getConnection(); 
            PreparedStatement ps = conn.prepareStatement("SELECT clase.fecha, clase.dia_semana, clase.inicio, clase.fin, " +
                    "clase.plazas_disponibles, clase.actividad FROM clase, actividad, monitor " +
                    "WHERE clase.actividad = actividad.nombre AND actividad.monitor = monitor.correo "
                    + "AND monitor.correo = ?");
            ps.setString(1, correo);
            
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
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return lista;
    }
    
    /*
     * Devuelve una lista con las clases asignadas a un monitor entre las fechas inicio y fin
     */
    public List<ClaseVO> listClasesMonitorPorFecha(String correo, LocalDate inicio, LocalDate fin) throws DAOException {
        Connection conn = null;
        List<ClaseVO> lista = new ArrayList<>();

        try {
            // Abrimos la conexión e inicializamos los parámetros 
            conn = PoolConnectionManager.getConnection(); 
            PreparedStatement ps = conn.prepareStatement("SELECT clase.fecha, clase.dia_semana, clase.inicio, clase.fin, " +
                                    "clase.plazas_disponibles, clase.actividad FROM sisinf_p2.clase, sisinf_p2.actividad  " +
                                    "WHERE clase.actividad = actividad.nombre AND actividad.monitor = ? " + 
                                    "AND clase.fecha <= ? AND clase.fecha >= ? ORDER BY clase.fecha ASC");
            ps.setString(1, correo);
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
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return lista;
    }

    /*
     * Devuelve una lista con las clases reservadas por un cliente
     */
    public List<ClaseVO> listClasesCliente(String correo) throws DAOException {
        Connection conn = null;
        List<ClaseVO> lista = new ArrayList<>();

        try {
            // Abrimos la conexión e inicializamos los parámetros 
            conn = PoolConnectionManager.getConnection(); 
            PreparedStatement ps = conn.prepareStatement("SELECT clase.fecha, clase.dia_semana, clase.inicio, clase.fin" +
                                    "clase.plazas_disponibles, clase.actividad FROM clase, reserva, cliente" +
                                    "WHERE clase.actividad = reserva.actividad AND clase.fecha = reserva.fecha" +
                                    "AND clase.dia_semana = reserva.dia_semana AND clase.inicio = reserva.hora AND reserva.cliente = ?");
            ps.setString(1, correo);
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
     * Devuelve una lista con las clases reservadas por un cliente entre las fechas inicio y fin
     */
    public List<ClaseVO> listClasesClientePorFecha(String correo, LocalDate inicio, LocalDate fin) throws DAOException {
        Connection conn = null;
        List<ClaseVO> lista = new ArrayList<>();

        try {
            // Abrimos la conexión e inicializamos los parámetros 
            conn = PoolConnectionManager.getConnection(); 
            PreparedStatement ps = conn.prepareStatement("SELECT clase.fecha, clase.dia_semana, clase.inicio, clase.fin, " +
                                    "clase.plazas_disponibles, clase.actividad FROM sisinf_p2.clase, sisinf_p2.reserva " +
                                    "WHERE clase.actividad = reserva.actividad AND clase.fecha = reserva.fecha " +
                                    "AND clase.inicio = reserva.hora AND reserva.cliente = ? " +
                                    "AND clase.fecha <= ? AND clase.fecha >= ? ORDER BY clase.fecha ASC");
            
            ps.setString(1, correo);
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
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return lista;
    }

    /*
     * Devuelve el numero de clases reservadas por un cliente
     */
    public Integer countReservasCliente(String correo) throws DAOException {
        Connection conn = null;
        int count = 0;
        try {
            // Obtener la conexión del pool de conexiones
            conn = PoolConnectionManager.getConnection();
    
            // Preparar la sentencia SQL para contar monitores
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM reservas where cliente = ?");
            ps.setString(1, correo);
    
            // Ejecutar la consulta
            ResultSet rset = ps.executeQuery();
    
            // Si no se eliminó ninguna fila, el usuario no existe
            if (rset.next()) {
                count = rset.getInt(1);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error al contar los clientes en la base de datos.", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        
        return count;
    }

    /*
     * Devuelve el numero total de clientes que hay en la BD
     */
    public Integer countClientes() throws DAOException {
        Connection conn = null;
        int count = 0;
        try {
            // Obtener la conexión del pool de conexiones
            conn = PoolConnectionManager.getConnection();
    
            // Preparar la sentencia SQL para contar monitores
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM cliente");
    
            // Ejecutar la consulta
            ResultSet rset = ps.executeQuery();
    
            // Si no se eliminó ninguna fila, el usuario no existe
            if (rset.next()) {
                count = rset.getInt(1);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error al contar los clientes en la base de datos.", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        
        return count;
    }

    /*
     * Devuelve el numero total de monitores que hay en la BD
     */
    public Integer countMonitores() throws DAOException {
        Connection conn = null;
        int count = 0;
        try {
            // Obtener la conexión del pool de conexiones
            conn = PoolConnectionManager.getConnection();
    
            // Preparar la sentencia SQL para contar monitores
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM monitor");
    
            // Ejecutar la consulta
            ResultSet rset = ps.executeQuery();
    
            // Si no se eliminó ninguna fila, el usuario no existe
            if (rset.next()) {
                count = rset.getInt(1);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            throw new DAOException("Error al contar los monitores en la base de datos.", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        
        return count; 
    }

    /*
     * Devuelve verdad si y solo si el monitor tiene asignada una actividad distinta de la dada, en el horario inicio-fin entre los dias f_ini y f_fin
     */
    public boolean hayClasesEnHorarioMonitor(String correo, DayOfWeek dia, LocalTime inicio, LocalTime fin, LocalDate f_ini, LocalDate f_fin, String actividad) throws DAOException {
        Connection conn = null;
        boolean resultado = false; 

        try {
            // Abrimos la conexión e inicializamos los parámetros 
            conn = PoolConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT DISTINCT clase.actividad FROM sisinf_p2.clase " +
                "JOIN sisinf_p2.actividad ON actividad.nombre = clase.actividad " +
                "WHERE " +
                "(" +
                "clase.inicio = ? OR clase.fin = ? OR " +
                "(clase.inicio > ? AND clase.fin < ?) OR " +
                "(clase.inicio < ? AND clase.fin > ?) OR " +
                "(clase.inicio > ? AND clase.fin > ? AND clase.inicio < ?) OR " +
                "(clase.inicio < ? AND clase.fin < ? AND clase.fin > ?)" +
                ") " +
                "AND clase.dia_semana = ? " +
                "AND actividad.monitor = ? " +
                "AND clase.fecha BETWEEN ? AND ? " +
                "AND NOT (actividad.nombre = ?)"
            );

            ps.setObject(1, inicio);
            ps.setObject(2, fin);
            ps.setObject(3, inicio);
            ps.setObject(4, fin);
            ps.setObject(5, inicio);
            ps.setObject(6, fin);
            ps.setObject(7, inicio);
            ps.setObject(8, fin);
            ps.setObject(9, fin);
            ps.setObject(10, inicio);
            ps.setObject(11, fin);
            ps.setObject(12, inicio);
            ps.setString(13, ClaseVO.DayOfWeekToString(dia));
            ps.setString(14, correo);
            ps.setObject(15, f_ini);
            ps.setObject(16, f_fin);
            ps.setObject(17, actividad);

            System.out.println("Consulta SQL: " + ps.toString());
            ResultSet rset = ps.executeQuery();

            // Verificamos si hay resultados
            resultado = rset.next();
        } catch (SQLException e) {
            throw new DAOException("Error de integridad en la base de datos", e);
        } finally {
            if (conn != null) {
                PoolConnectionManager.releaseConnection(conn);
            }
        }
        return resultado;
    }

}

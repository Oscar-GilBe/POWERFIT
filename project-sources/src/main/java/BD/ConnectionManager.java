package BD;
//package ...
import java.sql.*;

/* Clase que abstrae la conexion con la base de datos. */

public class ConnectionManager {
	// JDBC nombred el driver y URL de BD 
	private static final String JDBC_DRIVER = "org.postgresql.Driver";  
	private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=sisinf_p2";
	
	// Credenciales de la Base de Datos
	private static final String USER = "postgres";
	private static final String PASS = "sisinf123";
    //ALTER USER postgres WITH PASSWORD 'sisinf123';
	
	// Devuelve una nueva conexion.
	public final static Connection getConnection() throws SQLException {
		Connection conn = null;

		try{
			//STEP 1: Register JDBC driver
			Class.forName(JDBC_DRIVER);
			//STEP 2: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			return conn; 
		} catch (Exception e) {
			e.printStackTrace();
			return null; 
		} 
	}
	
	// Libera la conexion, devolviendola al pool 
	public final static void releaseConnection(Connection conn) throws SQLException {
		conn.close(); 
	}
}
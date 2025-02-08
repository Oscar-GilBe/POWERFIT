import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

import BD.ConnectionManager;
import Util.PasswordUtil;

public class EncryptPasswords {
    public static void main(String[] args) {
        // Conexión a la base de datos
    	Connection conn = null;

        try {
            conn = ConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT correo, contrasena FROM sisinf_p2.USUARIO");
    	    ResultSet rs = ps.executeQuery();
    	    
    	    // Procesar cada usuario para encriptar su contraseña
            while (rs.next()) {
                String correo = rs.getString("correo");
                String contrasena = rs.getString("contrasena");

                // Encriptar la contraseña
                String hash = PasswordUtil.hashPassword(contrasena);

                // Actualizar la contraseña en la base de datos
                updatePassword(conn, correo, hash);
                System.out.println("Contraseña encriptada y actualizada para: " + correo);
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar o procesar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para actualizar la contraseña en la base de datos
    private static void updatePassword(Connection conn, String correo, String hashedPassword) throws SQLException {
        String updateQuery = "UPDATE sisinf_p2.USUARIO SET contrasena = ? WHERE correo = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setString(1, hashedPassword);
            updateStmt.setString(2, correo);
            updateStmt.executeUpdate();
        }
    }
}
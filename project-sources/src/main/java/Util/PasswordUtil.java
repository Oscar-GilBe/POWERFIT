package Util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Método para hashear una contraseña
    public static String hashPassword(String password) {
        // Genera el hash usando un costo de 10 (es configurable)
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    // Método para verificar la contraseña
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
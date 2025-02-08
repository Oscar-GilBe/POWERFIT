package Exceptions;

// Extiende de Exception para crear una Checked Exception
public class DAOException extends Exception {
    // Constructor que recibe solo el mensaje de error
    public DAOException(String message) {
        super(message);
    }

    // Constructor que recibe un mensaje de error y la excepción original (causa)
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor que solo envuelve otra excepción
    public DAOException(Throwable cause) {
        super(cause);
    }
}

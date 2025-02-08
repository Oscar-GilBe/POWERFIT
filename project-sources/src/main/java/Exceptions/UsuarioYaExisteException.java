package Exceptions;

public class UsuarioYaExisteException extends DAOException {
    public UsuarioYaExisteException(String message) {
        super(message);
    }
}
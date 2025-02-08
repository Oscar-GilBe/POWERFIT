package Exceptions;

public class UsuarioNoEncontradoException extends DAOException {
    public UsuarioNoEncontradoException(String message) {
        super(message);
    }
}
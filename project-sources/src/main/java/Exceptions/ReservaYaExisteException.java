package Exceptions;

public class ReservaYaExisteException extends DAOException {
    public ReservaYaExisteException(String message) {
        super(message);
    }
}
package Exceptions;

public class ActividadYaExisteException extends DAOException {
    public ActividadYaExisteException(String message) {
        super(message);
    }
}
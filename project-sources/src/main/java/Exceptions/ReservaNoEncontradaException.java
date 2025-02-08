package Exceptions;

public class ReservaNoEncontradaException extends DAOException {
    public ReservaNoEncontradaException(String message) {
        super(message);
    }
}
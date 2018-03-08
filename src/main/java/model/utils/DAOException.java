package model.utils;

/**
 * DAO level exception.
 */
public class DAOException extends Exception {
    public DAOException() {
    }

    public DAOException(String message) {
        super(message);
    }
}

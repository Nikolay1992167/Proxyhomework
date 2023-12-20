package by.clevertec.exception;

public class CarSQLException extends RuntimeException{
    public CarSQLException(String message) {
        super("Error connecting to the database:" + message);
    }
}

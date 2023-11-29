package by.clevertec.exception;

public class ResourceSqlException extends RuntimeException {

    public ResourceSqlException() {
        super("Problem when executing the request!");
    }
}
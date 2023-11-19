package by.clevertec.exception;

public class ResourceSqlException extends RuntimeException {

    public ResourceSqlException() {
        super("Проблема при выполнении запроса!");
    }
}
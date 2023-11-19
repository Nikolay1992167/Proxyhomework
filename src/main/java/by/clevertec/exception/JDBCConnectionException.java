package by.clevertec.exception;

public class JDBCConnectionException extends RuntimeException {

    public JDBCConnectionException(String message) {
        super("Ошибка при подключении к базе данных:" + message);
    }
}

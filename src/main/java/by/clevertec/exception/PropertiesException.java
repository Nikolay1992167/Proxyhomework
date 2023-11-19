package by.clevertec.exception;

public class PropertiesException extends RuntimeException{

    public PropertiesException() {
        super("Не удалось прочитать данные!");
    }
}

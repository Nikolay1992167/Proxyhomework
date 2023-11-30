package by.clevertec.exception;

public class ProxyInvocationFailedException extends RuntimeException{

    public ProxyInvocationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}

package by.clevertec.exception;

public class PDFCreatingException extends RuntimeException {

    public PDFCreatingException() {
        super("Error when creating a pdf file!");
    }
}

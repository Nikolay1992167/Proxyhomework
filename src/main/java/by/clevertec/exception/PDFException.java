package by.clevertec.exception;

public class PDFException extends RuntimeException {

    public PDFException() {
        super("Error when creating a pdf file!");
    }
}

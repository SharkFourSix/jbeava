package lib.gintec_rdl.jbeava.validation.exceptions;

public class JBeavaException extends RuntimeException {
    public JBeavaException(String message) {
        super(message);
    }

    public JBeavaException(Exception e) {
        super(e);
    }

    public JBeavaException(String message, Exception e) {
        super(message, e);
    }
}

package core.mvc.exception;

public class HandlerNotFoundException extends RuntimeException {
    public HandlerNotFoundException() {
        super("handler not found");
    }
}

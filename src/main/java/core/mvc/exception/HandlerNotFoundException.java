package core.mvc.exception;

public class HandlerNotFoundException extends Throwable {
    public HandlerNotFoundException() {
    }

    public HandlerNotFoundException(String message) {
        super(message);
    }
}

package core.mvc;

import java.io.IOException;

public class ObjectMapperException extends RuntimeException {
    public ObjectMapperException() {
    }

    public ObjectMapperException(Throwable cause) {
        super(cause);
    }

    public ObjectMapperException(String message, Throwable e) {
        super(message, e);
    }
}

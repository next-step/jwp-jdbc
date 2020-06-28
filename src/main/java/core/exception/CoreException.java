package core.exception;

import lombok.Getter;

@Getter
public class CoreException extends RuntimeException {
    private String message;

    public CoreException(CoreExceptionStatus status, Throwable cause) {
        super(cause);
        this.message = status.getMessage();
    }

    public CoreException(CoreExceptionStatus status) {
        this.message = status.getMessage();
    }
}

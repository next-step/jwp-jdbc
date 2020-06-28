package core.exception;

import lombok.Getter;

@Getter
public enum CoreExceptionStatus {
    CLASS_NEW_INSTANCE_FAIL("class new instance fail"),
    INVALID_PARAMETERS("invalid parameters"),
    INVALID_PATH_VARIABLE("invalid path variables"),
    NOT_ALLOW_METHOD("not allow method");

    private String message;

    CoreExceptionStatus(String message) {
        this.message = message;
    }
}

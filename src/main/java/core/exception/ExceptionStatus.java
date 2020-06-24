package core.exception;

import lombok.Getter;

@Getter
public enum ExceptionStatus {
    JSON_PARSE_EXCEPTION("json parse fail");

    private String message;

    ExceptionStatus(String message) {
        this.message = message;
    }
}

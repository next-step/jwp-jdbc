package core.exception;

import lombok.Getter;

@Getter
public enum ExceptionStatus {
    JSON_PARSE_EXCEPTION("json parse fail"),

    JDBC_SET_AUTO_COMMIT_FAIL("jdbc set auto commit fail"),
    TRANSACTION_COMMIT_FAIL("transaction commit fail"),
    TRANSACTION_ROLLBACK_FAIL("transaction rollback fail"),
    UPDATE_fAIL("update query fail"),
    QUERY_fAIL("query fail");

    private String message;

    ExceptionStatus(String message) {
        this.message = message;
    }
}

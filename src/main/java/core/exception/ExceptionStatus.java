package core.exception;

import lombok.Getter;

@Getter
public enum ExceptionStatus {
    JSON_PARSE_EXCEPTION("json parse fail"),

    JDBC_SET_AUTO_COMMIT_FAIL("jdbc set auto commit fail"),
    TRANSACTION_COMMIT_FAIL("transaction commit fail"),
    TRANSACTION_ROLLBACK_FAIL("transaction rollback fail"),
    UPDATE_fAIL("update query fail"),
    QUERY_FOR_OBJECT_fAIL("query for object fail"),
    QUERY_FOR_LIST_fAIL("query for list fail"),
    NOT_CORRESPOND_PARAMETER_INDEX("parameterIndex does not correspond to a parameter"),
    INVALID_COLUMN_LABEL("columnLabel is not valid"),
    GET_OBJECT_FAIL("get object fail"),
    GET_OBJECT_LIST_FAIL("get object list fail");

    private String message;

    ExceptionStatus(String message) {
        this.message = message;
    }
}

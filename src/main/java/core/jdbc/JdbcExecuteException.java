package core.jdbc;

import org.springframework.dao.DataAccessException;

class JdbcExecuteException extends DataAccessException {

    private static final String ERROR_MESSAGE = "Jdbc 처리에 실패하였습니다";

    JdbcExecuteException(Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }
}

package core.jdbc.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SqlRunTimeException extends RuntimeException {
    public SqlRunTimeException(String msg) {
        super(msg);
    }
}

package next.exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException() {
        super("DB 연결에 실패 하였습니다.");
    }

    public DataAccessException(String message) {
        super(message);
    }
}

package core.jdbc;

public class IncorrectResultSizeDataAccessException extends DataAccessException {

    private static final String MESSAGE = "데이터 조회 결과가 유일하지 않습니다. (현재: %d)";

    public IncorrectResultSizeDataAccessException(int size) {
        super(String.format(MESSAGE, size));
    }
}

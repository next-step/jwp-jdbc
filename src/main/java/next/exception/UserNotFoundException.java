package next.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 사용자입니다: [%s]";

    public UserNotFoundException(String userId) {
        super(String.format(MESSAGE, userId));
    }
}

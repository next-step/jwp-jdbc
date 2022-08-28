package next.model.exception;

public class UpdateAuthenticationException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "다른 사용자의 정보를 수정할 수 없습니다.";

    public UpdateAuthenticationException() {
        super(DEFAULT_MESSAGE);
    }
}

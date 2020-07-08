package next.exception;

/**
 * Created By kjs4395 on 2020-07-07
 */
public class JdbcTemplateCloseException extends RuntimeException {
    public JdbcTemplateCloseException(String message) {
        super(message);
    }
}

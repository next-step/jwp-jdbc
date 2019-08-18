package next.support.utils.exceptions;

/**
 * Created by hspark on 2019-08-18.
 */
public class RequestBodyParsingException extends RuntimeException {
    public RequestBodyParsingException(String message) {
        super(message);
    }
}

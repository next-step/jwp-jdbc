package core.mvc.tobe;

/**
 * @author KingCjy
 */
public class PageNotFoundException extends RuntimeException {

    private String requestURI;

    public PageNotFoundException(String message, String requestURI) {
        super(message);
        this.requestURI = requestURI;
    }

    public String getRequestURI() {
        return requestURI;
    }
}

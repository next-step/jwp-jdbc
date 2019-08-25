package core.mvc;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

public class ResponseWriter {
    public static void created(final HttpServletResponse response, final String redirectUri) {
        final URI uri = URI.create(redirectUri);
        response.addHeader("location", uri.toString());
        response.setStatus(HttpStatus.CREATED.value());
    }

    public static void ok(final HttpServletResponse response) {
        response.setStatus(HttpStatus.OK.value());
    }
}

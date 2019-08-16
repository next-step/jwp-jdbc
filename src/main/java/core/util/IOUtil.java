package core.util;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class IOUtil {

    public static String getBodyFromServletRequest(ServletRequest servletRequest) throws ServletException {
        try {
            return servletRequest.getReader()
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new ServletException("reading request body failed", e);
        }
    }

}

package core.mvc;

import org.springframework.http.MediaType;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class RestRequestDispatcher {
    private static final String DATA = "data";

    public static void forward(final ServletRequest request, final ServletResponse response) throws ServletException, IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("content-type", MediaType.APPLICATION_JSON_VALUE);
        Object data = request.getAttribute(DATA);
        if (Objects.nonNull(data)) {
            response.getWriter().println(data);
        }
    }

}

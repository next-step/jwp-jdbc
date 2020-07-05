package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class RestRequestDispatcher {
    private static final String DATA = "data";
    private static final Logger logger = LoggerFactory.getLogger(RestRequestDispatcher.class);

    public static void forward(final ServletRequest request, final ServletResponse response) throws ServletException, IOException {
        responseHeader(request, response);

        Object data = request.getAttribute(DATA);
        if (Objects.nonNull(data)) {
            response.getWriter().println(data);
        }
    }

    private static void responseHeader(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("content-type", MediaType.APPLICATION_JSON_VALUE);

        if (httpServletRequest.getMethod().equals("POST")) {
            httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
            return;
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }

}

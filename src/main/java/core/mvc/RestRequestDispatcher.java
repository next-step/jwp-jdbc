package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class RestRequestDispatcher {
    private static final String DATA = "data";
    private static final Logger logger = LoggerFactory.getLogger(RestRequestDispatcher.class);

    public static void forward(final ServletRequest request, final ServletResponse response) throws ServletException, IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("content-type", MediaType.APPLICATION_JSON_VALUE);
        Object data = request.getAttribute(DATA);
        if (Objects.nonNull(data)) {
            response.getWriter().println(data);
        }
        logger.info(data.toString());
    }

}

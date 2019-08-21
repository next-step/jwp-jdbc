package study.httpServlet;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestTest {
    private static final Logger logger = LoggerFactory.getLogger(HttpServletRequestTest.class);

    @Test
    void name() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/api/users");
        mockRequest.setQueryString("userId=pobi");

        HttpServletRequest request = mockRequest;
        logger.debug(request.getRequestURI());
        logger.debug(request.getRequestURL().toString());
        logger.debug(request.getQueryString());
    }
}

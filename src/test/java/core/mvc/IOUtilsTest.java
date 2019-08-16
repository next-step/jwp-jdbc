package core.mvc;

import core.util.IOUtil;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IOUtilsTest {

    @Test
    void getBodyFromServletRequest() throws ServletException {
        String expected = "{\"name\"=\"jun\", \"email\"=\"test@test.com\"}";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(expected.getBytes());

        final String body = IOUtil.getBodyFromServletRequest(request);
        assertEquals(expected, body);
    }

}

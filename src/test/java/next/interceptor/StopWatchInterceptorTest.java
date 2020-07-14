package next.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created By kjs4395 on 2020-07-13
 */
public class StopWatchInterceptorTest {

    @Test
    void interceptorTest() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Interceptor interceptor = new StopWatchInterceptor();
        interceptor.preHandle(request, response);
        assertNotNull(request.getAttribute("stopWatch"));
    }
}

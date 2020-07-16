package core.mvc.interceptor;

import next.controller.HomeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class InterceptorExecutorTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private InterceptorRegistry interceptorRegistry;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        interceptorRegistry = new InterceptorRegistry();
    }

    @Test
    void emptyInterceptor() {
        InterceptorExecutor interceptorExecutor = new InterceptorExecutor(interceptorRegistry);
        assertThatIllegalArgumentException().isThrownBy(() -> interceptorExecutor.pre(request, response, new HomeController()));
    }
}

package next.dao.interceptor;

import next.controller.HomeController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class HandlerInterceptorCompositeTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private HandlerInterceptorComposite handlerInterceptorComposite;

    @BeforeEach
    void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        handlerInterceptorComposite = HandlerInterceptorComposite.getInstance();
    }

    @Test
    void preHandle() {
        boolean preHandle = handlerInterceptorComposite.preHandle(request, response, new HomeController());
        Assertions.assertThat(preHandle).isTrue();
    }

    @Test
    void postHandle() {
        handlerInterceptorComposite.preHandle(request, response, new HomeController());
    }
}

package core.interceptor;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class RunningTimeInterceptorTest {
    private RunningTimeInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private HandlerExecution handlerExecution;

    @BeforeEach
    void setUp() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        interceptor = new RunningTimeInterceptor();
        request = new MockHttpServletRequest("GET", "/users/profile");
        response = new MockHttpServletResponse();
        handlerExecution = annotationHandlerMapping.getHandler(request);
    }

    @Test
    void preHandle() throws Exception {
        boolean actual = interceptor.preHandle(request, response, handlerExecution);
        assertThat(actual).isTrue();
        assertThat(request.getAttribute("startAt")).isNotNull();
    }

    @Test
    void postHandle() throws Exception {
        long expected = 10;
        ModelAndView mav = new ModelAndView();
        interceptor.preHandle(request, response, handlerExecution);
        Thread.sleep(expected);
        interceptor.postHandle(request, response, handlerExecution, mav);

        Long startAt = (Long) request.getAttribute("startAt");
        Long endAt = (Long) request.getAttribute("endAt");

        assertThat(endAt).isNotNull();
        assertThat(endAt - startAt).isGreaterThan(expected);
    }

    @Test
    void afterCompletion() throws Exception {
        long expected = 10;
        ModelAndView mav = new ModelAndView();
        interceptor.preHandle(request, response, handlerExecution);
        Thread.sleep(expected);
        interceptor.postHandle(request, response, handlerExecution, mav);

        interceptor.afterCompletion(request, response, handlerExecution, null);
    }
}

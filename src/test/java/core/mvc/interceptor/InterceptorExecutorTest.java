package core.mvc.interceptor;

import core.mvc.ModelAndView;
import next.controller.HomeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

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

    @Test
    void handlerTest() {
        interceptorRegistry.addInterceptor(new Interceptor() {
            @Override
            public boolean pre(final HttpServletRequest request, final HttpServletResponse response, final Object object) {
                return true;
            }

            @Override
            public void post(final HttpServletRequest request, final HttpServletResponse response, final Object object, final ModelAndView modelAndView) {
                if (pre(request, response, object)) {
                    request.setAttribute("name", "seongju");
                }
            }
        });

        interceptorRegistry.addInterceptor(new Interceptor() {
            @Override
            public boolean pre(final HttpServletRequest request, final HttpServletResponse response, final Object object) {
                return false;
            }

            @Override
            public void post(final HttpServletRequest request, final HttpServletResponse response, final Object object, final ModelAndView modelAndView) {
                if (pre(request, response, object)) {
                    request.setAttribute("name", "kim");
                }
            }
        });

        InterceptorExecutor interceptorExecutor = new InterceptorExecutor(interceptorRegistry);
        interceptorExecutor.post(request, response, new HomeController(), new ModelAndView());
        final String name = (String) request.getAttribute("name");

        assertThat(name).isEqualTo("seongju");
    }
}

package core.mvc.interceptor;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import next.controller.HomeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InterceptorRegistryTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void preHandlerTest() {
        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
        interceptorRegistry.addInterceptor(new Interceptor() {
            @Override
            public boolean pre(final HttpServletRequest request, final HttpServletResponse response, final Object object) {
                request.setAttribute("id", "test");
                response.setStatus(HttpServletResponse.SC_OK);
                return object instanceof Controller;
            }

            @Override
            public void post(final HttpServletRequest request, final HttpServletResponse response, final Object object, final ModelAndView modelAndView) {
            }
        });

        interceptorRegistry.pre(request, response, new HomeController());
        final String actualId = (String) request.getAttribute("id");
        final int actualResponse = response.getStatus();

        assertAll("interceptor registry test", () -> {
            assertThat(actualId).isEqualTo("test");
            assertThat(actualResponse).isEqualTo(200);
        });
    }
}

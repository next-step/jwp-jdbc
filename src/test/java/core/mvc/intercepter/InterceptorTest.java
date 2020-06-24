package core.mvc.intercepter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인터셉터 테스트")
class InterceptorTest {
    private static final String TEST = "test";

    private TestInterceptor testInterceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setEnv() {
        testInterceptor = new TestInterceptor();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("인터셉터는 controller 단 기능 수행 이전에 역할을 수행할 수 있어야 한다")
    void preProcess() {
        assertThat(request.getAttribute(TEST)).isNull();
        testInterceptor.preProcess(request, response);
        assertThat(request.getAttribute(TEST)).isNotNull();
        assertThat(request.getAttribute(TEST)).isEqualTo(true);
    }

    @Test
    @DisplayName("인터셉터는 controller 단 기능 수행 이후에 역할을 수행할 수 있어야 한다.")
    void postProcess() {
        assertThat(request.getAttribute(TEST)).isNull();
        testInterceptor.postProcess(request, response);
        assertThat(request.getAttribute(TEST)).isNotNull();
        assertThat(request.getAttribute(TEST)).isEqualTo(false);
    }

    private static class TestInterceptor implements Interceptor {
        @Override
        public void preProcess(HttpServletRequest request, HttpServletResponse response) {
            request.setAttribute(TEST, true);
        }

        @Override
        public void postProcess(HttpServletRequest request, HttpServletResponse response) {
            request.setAttribute(TEST, false);
        }
    }
}
package core.mvc.interceptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class InterceptorRegistrationTest {

    @DisplayName("포함되는 패턴과 배제되는 패턴 체크하기")
    @Test
    void matches() {
        /* given */
        InterceptorRegistration interceptorRegistration = new InterceptorRegistration(new MockHandlerInterceptor());
        interceptorRegistration.addPathPatterns("/api/users/**", "/login");
        interceptorRegistration.excludePathPatterns("/api/users/test");

        /* when */
        boolean result1 = interceptorRegistration.supportsPattern("/api/users");
        boolean result2 = interceptorRegistration.supportsPattern("/api/users/test");

        /* then */
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    private static class MockHandlerInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            return true;
        }

    }

}

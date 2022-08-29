package core.web.interceptor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InterceptorRegistrationTest {

    @Test
    void matchesTest() {
        HandlerInterceptor interceptor = new ExecutionTimeCheckHandlerInterceptor();
        InterceptorRegistration registration = new InterceptorRegistration(interceptor)
            .addPathPatterns("/path1/test");

        assertThat(registration.matches("/path1/test")).isTrue();
    }

    @Test
    void allMatchesTest() {
        HandlerInterceptor interceptor = new ExecutionTimeCheckHandlerInterceptor();
        InterceptorRegistration registration = new InterceptorRegistration(interceptor)
            .addPathPatterns("/**");

        assertThat(registration.matches("/test1/path/test")).isTrue();
        assertThat(registration.matches("/test1/test")).isTrue();
        assertThat(registration.matches("/test1")).isTrue();
    }

}

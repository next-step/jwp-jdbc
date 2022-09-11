package core.mvc.interceptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InterceptorRegistrationTest {
    @DisplayName("요청 url 에 맞는 인터셉터를 반환한다.")
    @Test
    void includePatternsInterceptor() {
        InterceptorRegistration registration = new InterceptorRegistration(new TimeMeasuringInterceptor());
        registration.addPathPatterns("/api/users");
        Optional<HandlerInterceptor> matchedInterceptor = registration.getMatchedInterceptor("/api/users");

        assertThat(matchedInterceptor.get()).isInstanceOf(TimeMeasuringInterceptor.class);
    }

    @DisplayName("요청 url(wild card) 에 맞는 인터셉터를 반환한다.")
    @Test
    void includeWildCardPatternsInterceptor() {
        InterceptorRegistration registration = new InterceptorRegistration(new TimeMeasuringInterceptor());
        registration.addPathPatterns("/**");
        Optional<HandlerInterceptor> matchedInterceptor = registration.getMatchedInterceptor("/api/users");

        assertThat(matchedInterceptor.get()).isInstanceOf(TimeMeasuringInterceptor.class);
    }

    @DisplayName("제외된 요청 url 을 요청할 경우 Optional.empty() 를 반환한다.")
    @Test
    void excludePatternsInterceptor() {
        InterceptorRegistration registration = new InterceptorRegistration(new TimeMeasuringInterceptor());
        registration.excludePathPatterns("/api/users");
        Optional<HandlerInterceptor> matchedInterceptor = registration.getMatchedInterceptor("/api/users");

        assertThat(matchedInterceptor).isEmpty();
    }
}
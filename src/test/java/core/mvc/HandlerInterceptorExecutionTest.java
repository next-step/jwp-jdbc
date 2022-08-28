package core.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.tobe.mock.MockChainHandlerInterceptor;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerInterceptorExecutionTest {

    private MockChainHandlerInterceptor mockInterceptor;
    private HandlerInterceptorExecution execution;

    @BeforeEach
    void setUp() {
        mockInterceptor = new MockChainHandlerInterceptor();
        execution = new HandlerInterceptorExecution(mockInterceptor);
    }

    @DisplayName("인터셉터 실행기 생성")
    @Test
    void constructor() {
        final HandlerInterceptorExecution expected = new HandlerInterceptorExecution(mockInterceptor);

        assertThat(execution).isEqualTo(expected);
    }

    @DisplayName("Interceptor가 수행 될 PathPattern을 등록할 수 있다")
    @Test
    void add_path_pattern() {
        execution.addPathPatterns("/users");

        final HandlerInterceptorExecution expected = new HandlerInterceptorExecution(mockInterceptor);
        expected.addPathPatterns("/users");

        assertThat(execution).isEqualTo(expected);
    }

    @DisplayName("Interceptor가 수행 될 PathPattern을 여러 개 등록할 수 있다")
    @Test
    void add_path_patterns() {
        execution.addPathPatterns("/users", "/members/{id}");

        final HandlerInterceptorExecution expected = new HandlerInterceptorExecution(mockInterceptor);
        expected.addPathPatterns("/users", "/members/{id}");

        assertThat(execution).isEqualTo(expected);
    }

    @DisplayName("등록된 pattern과 일치하는 Interceptor를 반환한다")
    @Test
    void returns_an_interceptor_that_matches_added_pattern() {
        execution.addPathPatterns("/users/{id}");

        final Optional<HandlerInterceptor> actual = execution.getHandlerInterceptor("/users/1");

        assertThat(actual).isPresent().get().isEqualTo(mockInterceptor);
    }

    @DisplayName("pattern이 설정되지 않은 Interceptor는 모든 url에 반환된다")
    @Test
    void returns_an_interceptor() {
        final Optional<HandlerInterceptor> actual = execution.getHandlerInterceptor("/user/1");

        assertThat(actual).isPresent().get().isEqualTo(mockInterceptor);
    }

    @DisplayName("등록된 pattern과 일치하지 않는 경우 Interceptor를 반환하지 않는다")
    @Test
    void do_nothing_interceptor() {
        execution.addPathPatterns("/users");

        final Optional<HandlerInterceptor> actual = execution.getHandlerInterceptor("/members");

        assertThat(actual).isNotPresent();
    }
}

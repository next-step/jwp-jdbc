package core.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.mvc.tobe.mock.MockChainHandlerInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerInterceptorExecutionTest {

    private  MockChainHandlerInterceptor mockInterceptor;
    private  HandlerInterceptorExecution execution;

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
        execution.addPathPatterns("/users", "/members/**");

        final HandlerInterceptorExecution expected = new HandlerInterceptorExecution(mockInterceptor);
        expected.addPathPatterns("/users", "/members/**");

        assertThat(execution).isEqualTo(expected);
    }

}

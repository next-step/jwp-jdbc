package core.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.tobe.mock.MockChainHandlerInterceptor;
import core.mvc.tobe.mock.MockNoChainHandlerInterceptor;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerInterceptorRegistry2Test {

    private final MockChainHandlerInterceptor mockChainHandlerInterceptor = new MockChainHandlerInterceptor();
    private final MockNoChainHandlerInterceptor mockNoChainHandlerInterceptor = new MockNoChainHandlerInterceptor();


    @DisplayName("인터셉터를 등록하면 Execution을 반환한다")
    @Test
    void return_execution() {
        final HandlerInterceptorRegistry2 registry = new HandlerInterceptorRegistry2();

        final HandlerInterceptorExecution actual = registry.addInterceptor(mockChainHandlerInterceptor);

        final HandlerInterceptorExecution expected = new HandlerInterceptorExecution(mockChainHandlerInterceptor);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("PathPattern이 일치하는 모든 Interceptor를 반환한다")
    @Test
    void pattern_matched_interceptors() {
        final HandlerInterceptorRegistry2 registry = new HandlerInterceptorRegistry2();

        registry.addInterceptor(mockChainHandlerInterceptor).addPathPatterns("/users/{id}");
        registry.addInterceptor(mockNoChainHandlerInterceptor);

        final List<HandlerInterceptor> handlerInterceptors = registry.urlMatchInterceptors("/users/1");

        assertThat(handlerInterceptors).containsExactly(
            mockChainHandlerInterceptor, mockNoChainHandlerInterceptor
        );
    }

    @DisplayName("PathPattern이 일치하지 않으면 Interceptor를 반환하지 않는다")
    @Test
    void pattern_not_matched_interceptors() {
        final HandlerInterceptorRegistry2 registry = new HandlerInterceptorRegistry2();

        registry.addInterceptor(mockChainHandlerInterceptor).addPathPatterns("/users/{id}");
        registry.addInterceptor(mockNoChainHandlerInterceptor).addPathPatterns("/members/{id}");

        final List<HandlerInterceptor> actual = registry.urlMatchInterceptors("/email/1");

        assertThat(actual).isEmpty();
    }
}

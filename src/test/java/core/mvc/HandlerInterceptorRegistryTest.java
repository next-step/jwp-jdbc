package core.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.tobe.mock.MockChainHandlerInterceptor;
import core.mvc.tobe.mock.MockNoChainHandlerInterceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerInterceptorRegistryTest {

    @DisplayName("인터셉터를 추가할 수 있다")
    @Test
    void add_interceptor() {
        final HandlerInterceptorRegistry handlerInterceptorRegistry = new HandlerInterceptorRegistry();

        handlerInterceptorRegistry.addInterceptor(new MockChainHandlerInterceptor());
        handlerInterceptorRegistry.addInterceptor(new MockNoChainHandlerInterceptor());

        final boolean actual = handlerInterceptorRegistry.hasInterceptor();

        assertThat(actual).isTrue();
    }

    @DisplayName("인터셉터 포함 여부를 알 수 있다")
    @Test
    void empty_interceptor_registry() {
        final HandlerInterceptorRegistry handlerInterceptorRegistry = new HandlerInterceptorRegistry();

        final boolean actual = handlerInterceptorRegistry.hasInterceptor();

        assertThat(actual).isFalse();
    }
}

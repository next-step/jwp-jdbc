package core.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.mvc.tobe.mock.MockChainHandlerInterceptor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerInterceptorExecutionTest {

    @DisplayName("인터셉터 실행기 생성")
    @Test
    void constructor() {
        final MockChainHandlerInterceptor mockInterceptor = new MockChainHandlerInterceptor();
        final HandlerInterceptorExecution actual = new HandlerInterceptorExecution(mockInterceptor);

        assertThat(actual).isEqualTo(new HandlerInterceptorExecution(mockInterceptor));
    }



}

package core.mvc.tobe.interceptor;

import core.mvc.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class InterceptorChainTest {
    private static final int INTERCEPTOR_COUNT = 5;
    private List<HandlerInterceptor> interceptors = new ArrayList<>();

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Object handler;
    private ModelAndView modelAndView;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < INTERCEPTOR_COUNT; ++i) {
            interceptors.add(mock(TestInterceptor.class));
        }

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        handler = new Object();
        modelAndView = new ModelAndView();
    }

    @Test
    @DisplayName("Interceptor가 존재하지 않을 때의 테스트")
    void applyPreHandle_forNullInterceptor() throws Exception {
        InterceptorChain interceptorChain = new InterceptorChain(null);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();

        assertThat(interceptorChain.applyPreHandle(request, response, handler)).isTrue();
    }

    @Test
    @DisplayName("Interceptor preHandle 호출 순서 테스트")
    void applyPreHandle_orders() throws Exception {
        interceptors.forEach(mockPreHandle());

        InterceptorChain interceptorChain = new InterceptorChain(interceptors);
        interceptorChain.applyPreHandle(request, response, handler);

        InOrder inOrder = Mockito.inOrder(interceptors.toArray());

        interceptors.forEach(verifyPreHandle(inOrder));
    }

    private Consumer<HandlerInterceptor> mockPreHandle() {
        return interceptor -> {
            try {
                Mockito.doReturn(true).when(interceptor).preHandle(request, response, handler);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private Consumer<HandlerInterceptor> verifyPreHandle(InOrder inOrder) {
        return interceptor -> {
            try {
                inOrder.verify(interceptor).preHandle(request, response, handler);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Test
    @DisplayName("Interceptor postHandle 호출 순서 테스트")
    void applyPostHandle_orders() throws Exception {
        interceptors.forEach(mockPostHandle());

        InterceptorChain interceptorChain = new InterceptorChain(interceptors);
        interceptorChain.applyPostHandle(request, response, handler, modelAndView);

        InOrder inOrder = Mockito.inOrder(interceptors.toArray());

        for(int i = interceptors.size()-1; i >= 0; --i) {
            verifyPostHandle(inOrder, interceptors.get(i));
        }
    }

    private Consumer<HandlerInterceptor> mockPostHandle() {
        return interceptor -> {
            try {
                Mockito.doNothing().when(interceptor).postHandle(request, response, handler, modelAndView);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private void verifyPostHandle(InOrder inOrder, HandlerInterceptor interceptor) throws Exception {
        inOrder.verify(interceptor).postHandle(request, response, handler, modelAndView);
    }

    @Test
    @DisplayName("Interceptor afterCompletion 호출 순서 테스트")
    void applyAfterCompletion_orders() throws Exception {
        interceptors.forEach(mockAfterCompletion());

        InterceptorChain interceptorChain = new InterceptorChain(interceptors);
        interceptorChain.applyAfterCompletion(request, response, handler, null);

        InOrder inOrder = Mockito.inOrder(interceptors.toArray());

        for(int i = interceptors.size()-1; i >= 0; --i) {
            verifyAfterCompletion(inOrder, interceptors.get(i));
        }
    }

    private Consumer<HandlerInterceptor> mockAfterCompletion() {
        return interceptor -> {
            try {
                Mockito.doNothing().when(interceptor).afterCompletion(request, response, handler, null);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private void verifyAfterCompletion(InOrder inOrder, HandlerInterceptor interceptor) throws Exception {
        inOrder.verify(interceptor).afterCompletion(request, response, handler, null);
    }
}
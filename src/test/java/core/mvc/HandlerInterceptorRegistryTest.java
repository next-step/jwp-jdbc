package core.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import core.di.factory.example.QnaController;
import core.mvc.tobe.mock.MockChainHandlerInterceptor;
import core.mvc.tobe.mock.MockNoChainHandlerInterceptor;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class HandlerInterceptorRegistryTest {

    private HandlerInterceptorRegistry handlerInterceptorRegistry;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        handlerInterceptorRegistry = new HandlerInterceptorRegistry();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @DisplayName("인터셉터를 추가할 수 있다")
    @Test
    void add_interceptor() {
        handlerInterceptorRegistry.addInterceptor(new MockChainHandlerInterceptor());
        handlerInterceptorRegistry.addInterceptor(new MockNoChainHandlerInterceptor());

        final boolean actual = handlerInterceptorRegistry.hasInterceptor();

        assertThat(actual).isTrue();
    }

    @DisplayName("인터셉터 포함 여부를 알 수 있다")
    @Test
    void empty_interceptor_registry() {
        final boolean actual = handlerInterceptorRegistry.hasInterceptor();

        assertThat(actual).isFalse();
    }

    @DisplayName("추가된 Interceptor 의 순서대로 preHandle 메서드를 수행한다")
    @Test
    void invoke_pre_handle_method_in_order() throws Exception {
        // given
        handlerInterceptorRegistry.addInterceptor(times100ChainInterceptor());
        handlerInterceptorRegistry.addInterceptor(minus100ChainInterceptor());

        request.setAttribute("value", 10);

        final boolean actual = handlerInterceptorRegistry.applyPreHandle(request, response, createMockQnaController());
        final int valueActual = (int) request.getAttribute("value");

        // then
        assertThat(actual).isTrue();
        assertThat(valueActual).isEqualTo(10 * 100 - 100);
    }

    @DisplayName("추가된 Interceptor 의 순서대로 preHandle 메서드를 수행하고 true 를 리턴한다")
    @Test
    void true_return_invoke_pre_handle_method_in_order() throws Exception {
        // given
        handlerInterceptorRegistry.addInterceptor(minus100ChainInterceptor());
        handlerInterceptorRegistry.addInterceptor(times100ChainInterceptor());

        request.setAttribute("value", 10);

        // when
        final boolean actual = handlerInterceptorRegistry.applyPreHandle(request, response, createMockQnaController());
        final int valueActual = (int) request.getAttribute("value");

        // then
        assertThat(actual).isTrue();
        assertThat(valueActual).isEqualTo((10 - 100) * 100);
    }

    @DisplayName("preHandle 메서드 수행 중 false 리턴이 있는 경우 다음 단계를 진행하지 않고 false 를 리턴한다 ")
    @Test
    void if_false_return_then_does_not_proceed_to_the_next_step() throws Exception {
        // given
        handlerInterceptorRegistry.addInterceptor(minus20NoChainInterceptor());
        handlerInterceptorRegistry.addInterceptor(times100ChainInterceptor());

        request.setAttribute("value", 20);

        // when
        final boolean actual = handlerInterceptorRegistry.applyPreHandle(request, response, createMockQnaController());
        final int valueActual = (int) request.getAttribute("value");

        // then
        assertThat(actual).isFalse();
        assertThat(valueActual).isZero();
    }

    @DisplayName("추가된 Interceptor 의 순서대로 postHandle 메서드를 수행한다")
    @Test
    void invoke_post_handle_method_in_order() throws Exception {
        // given
        handlerInterceptorRegistry.addInterceptor(times100ChainInterceptor());
        handlerInterceptorRegistry.addInterceptor(minus100ChainInterceptor());

        request.setAttribute("value", 10);

        handlerInterceptorRegistry.applyPostHandle(request, response, createMockQnaController());
        final int valueActual = (int) request.getAttribute("value");

        // then
        assertThat(valueActual).isEqualTo(10 * 100 - 100);
    }

    @DisplayName("추가된 Interceptor 의 순서대로 afterCompletion 메서드를 수행한다")
    @Test
    void invoke_after_completion_method_in_order() throws Exception {
        // given
        handlerInterceptorRegistry.addInterceptor(times100ChainInterceptor());
        handlerInterceptorRegistry.addInterceptor(minus100ChainInterceptor());

        request.setAttribute("value", 10);

        handlerInterceptorRegistry.applyAfterCompletion(request, response, createMockQnaController(), null);
        final int valueActual = (int) request.getAttribute("value");

        // then
        assertThat(valueActual).isEqualTo(10 * 100 - 100);
    }

    private static QnaController createMockQnaController() {
        return new QnaController(null);
    }

    private static HandlerInterceptor minus100ChainInterceptor() {
        return new HandlerInterceptor() {
            @Override
            public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
                final int value = (int) request.getAttribute("value");
                request.setAttribute("value", value - 100);
                return true;
            }

            @Override
            public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
                final int value = (int) request.getAttribute("value");
                request.setAttribute("value", value - 100);
            }

            @Override
            public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception exception) {
                final int value = (int) request.getAttribute("value");
                request.setAttribute("value", value - 100);
            }
        };
    }

    private static HandlerInterceptor minus20NoChainInterceptor() {
        return new HandlerInterceptor() {
            @Override
            public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
                final int value = (int) request.getAttribute("value");
                request.setAttribute("value", value - 20);
                return false;
            }

        };
    }

    private static HandlerInterceptor times100ChainInterceptor() {
        return new HandlerInterceptor() {
            @Override
            public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
                final int value = (int) request.getAttribute("value");
                request.setAttribute("value", value * 100);
                return true;
            }

            @Override
            public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
                final int value = (int) request.getAttribute("value");
                request.setAttribute("value", value * 100);
            }

            @Override
            public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception exception) {
                final int value = (int) request.getAttribute("value");
                request.setAttribute("value", value * 100);
            }
        };
    }


    @DisplayName("stream allMatch test, false 가 나오는 순간 더 이상 진행하지 않는다")
    @Test
    void allMatch() {
        final List<Matcher> matchers = Arrays.asList(
            new Matcher(1, true),
            new Matcher(2, true),
            new Matcher(3, false),
            new Matcher(4, true),
            new Matcher(5, true));

        final boolean allMatch = matchers.stream()
            .allMatch(Matcher::isMatch);

        assertThat(allMatch).isFalse();
    }

    static class Matcher {
        private final int number;
        private final boolean match;

        Matcher(final int number, final boolean match) {
            this.number = number;
            this.match = match;
        }

        public boolean isMatch() {
            System.out.println(this);
            return match;
        }

        @Override
        public String toString() {
            return "Matcher{" +
                "number=" + number +
                ", match=" + match +
                '}';
        }
    }
}

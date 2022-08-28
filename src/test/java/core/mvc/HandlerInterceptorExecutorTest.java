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

class HandlerInterceptorExecutorTest {

    private HandlerInterceptorExecutor handlerInterceptorExecutor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @DisplayName("인터셉터를 추가하여 인터셉터 실행 객체를 생성한다")
    @Test
    void constructor() {
        handlerInterceptorExecutor = new HandlerInterceptorExecutor(
          List.of(new MockChainHandlerInterceptor(), new MockNoChainHandlerInterceptor())
        );

        final boolean actual = handlerInterceptorExecutor.isExecutable();

        assertThat(actual).isTrue();
    }

    @DisplayName("추가된 Interceptor 의 순서대로 preHandle 메서드를 수행한다")
    @Test
    void invoke_pre_handle_method_in_order() throws Exception {
        // given
        handlerInterceptorExecutor = new HandlerInterceptorExecutor(
            List.of(times100ChainInterceptor(), minus100ChainInterceptor())
        );

        request.setAttribute("value", 10);

        final boolean actual = handlerInterceptorExecutor.applyPreHandle(request, response, createMockQnaController());
        final int valueActual = (int) request.getAttribute("value");

        // then
        assertThat(actual).isTrue();
        assertThat(valueActual).isEqualTo(10 * 100 - 100);
    }

    @DisplayName("추가된 Interceptor 의 순서대로 preHandle 메서드를 수행하고 true 를 리턴한다")
    @Test
    void true_return_invoke_pre_handle_method_in_order() throws Exception {
        // given
        handlerInterceptorExecutor = new HandlerInterceptorExecutor(
            List.of(minus100ChainInterceptor(), times100ChainInterceptor())
        );

        request.setAttribute("value", 10);

        // when
        final boolean actual = handlerInterceptorExecutor.applyPreHandle(request, response, createMockQnaController());
        final int valueActual = (int) request.getAttribute("value");

        // then
        assertThat(actual).isTrue();
        assertThat(valueActual).isEqualTo((10 - 100) * 100);
    }

    @DisplayName("preHandle 메서드 수행 중 false 리턴이 있는 경우 다음 단계를 진행하지 않고 false 를 리턴한다 ")
    @Test
    void if_false_return_then_does_not_proceed_to_the_next_step() throws Exception {
        // given
        handlerInterceptorExecutor = new HandlerInterceptorExecutor(
            List.of(minus20NoChainInterceptor(), times100ChainInterceptor())
        );

        request.setAttribute("value", 20);

        // when
        final boolean actual = handlerInterceptorExecutor.applyPreHandle(request, response, createMockQnaController());
        final int valueActual = (int) request.getAttribute("value");

        // then
        assertThat(actual).isFalse();
        assertThat(valueActual).isZero();
    }

    @DisplayName("추가된 Interceptor 의 순서대로 postHandle 메서드를 수행한다")
    @Test
    void invoke_post_handle_method_in_order() throws Exception {
        // given
        handlerInterceptorExecutor = new HandlerInterceptorExecutor(
            List.of(times100ChainInterceptor(), minus100ChainInterceptor())
        );

        request.setAttribute("value", 10);

        handlerInterceptorExecutor.applyPostHandle(request, response, createMockQnaController());
        final int valueActual = (int) request.getAttribute("value");

        // then
        assertThat(valueActual).isEqualTo(10 * 100 - 100);
    }

    @DisplayName("추가된 Interceptor 의 순서대로 afterCompletion 메서드를 수행한다")
    @Test
    void invoke_after_completion_method_in_order() throws Exception {
        // given
        handlerInterceptorExecutor = new HandlerInterceptorExecutor(
            List.of(times100ChainInterceptor(), minus100ChainInterceptor())
        );

        request.setAttribute("value", 10);

        handlerInterceptorExecutor.applyAfterCompletion(request, response, createMockQnaController(), null);
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

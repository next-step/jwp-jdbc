package core.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class InterceptorsTest {
    private static final Logger logger = LoggerFactory.getLogger(InterceptorsTest.class);

    @DisplayName("add 메서드를 통해 인터셉터를 추가할 수 있다.")
    @Test
    void addInterceptor() {
        Interceptors interceptors = new Interceptors();
        List<Interceptor> result = (List<Interceptor>) ReflectionTestUtils.getField(interceptors, "store");

        interceptors.add(new MockInterceptor());

        assertThat(result).hasSize(1);
    }

    @DisplayName("stream 메서드를 통해 Interceptor를 스트림화 할 수 있다.")
    @Test
    void streamInterceptors() {
        Interceptors interceptors1 = new Interceptors();
        Interceptors interceptors2 = new Interceptors();
        interceptors1.add(new MockInterceptor());
        interceptors1.add(new MockInterceptor());
        interceptors2.add(new MockInterceptor());
        interceptors2.add(new MockInterceptor());

        List<Interceptor> result = Stream.of(interceptors1, interceptors2)
                .flatMap(Interceptors::stream)
                .collect(Collectors.toList());

        assertThat(result).hasSize(4);
    }


    @DisplayName("preHandle 메서드는 일급컬렉션이 참조하는 인터셉터들의 preHandle 메서드를 모두 실행시킨다.")
    @Test
    void preHandle() throws Exception {
        AtomicInteger atom = new AtomicInteger(0);
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resq = new MockHttpServletResponse();
        Interceptors interceptors = new Interceptors();

        interceptors.add(new Interceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                atom.addAndGet(1);
                return true;
            }
        });

        interceptors.preHandle(req, resq, null);

        assertThat(atom.get()).isEqualTo(1);
    }

    @DisplayName("postHandle 메서드는 일급컬렉션이 참조하는 인터셉터들의 postHandle 메서드를 모두 실행시킨다.")
    @Test
    void postHandle() throws Exception {
        AtomicInteger atom = new AtomicInteger(0);
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resq = new MockHttpServletResponse();
        Interceptors interceptors = new Interceptors();

        interceptors.add(new Interceptor() {
            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                atom.addAndGet(1);
            }
        });

        interceptors.postHandle(req, resq, null, null);

        assertThat(atom.get()).isEqualTo(1);
    }

    @DisplayName("afterCompletion 메서드는 일급컬렉션이 참조하는 인터셉터들의 afterCompletion 메서드를 모두 실행시킨다.")
    @Test
    void afterCompletion() throws Exception {
        AtomicInteger atom = new AtomicInteger(0);
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resq = new MockHttpServletResponse();
        Interceptors interceptors = new Interceptors();

        interceptors.add(new Interceptor() {
            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                atom.addAndGet(1);
            }
        });

        interceptors.afterCompletion(req, resq, null, null);

        assertThat(atom.get()).isEqualTo(1);
    }


    private static class MockInterceptor implements Interceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            return true;
        }

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
            logger.info("added interceptor postHandle");
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
            logger.info("added interceptor afterCompletion");
        }
    }

}

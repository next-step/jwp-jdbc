package core.mvc.intercepter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("인터셉터들 테스트")
class InterceptorsTest {
    private final List<Interceptor> interceptorList = Arrays.asList(new TestInterceptor1(), new TestInterceptor2());
    private final Interceptors interceptors = new Interceptors(interceptorList);
    private static final String TEST = "test";

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setEnv() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("interceptors 는 여러 인터셉터를 가지고 초기화 할 수 있다.")
    void init() {
        assertThatCode(() -> new Interceptors(this.interceptorList))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("interceptors 를 초기화 할때 null 을 넘기면 예외가 발생한다")
    void initFail() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Interceptors(null));
    }

    @Test
    @DisplayName("interceptors preProcess 호출시 먼저 선언된 인터셉터의 preProcess 부터 차례대로 호출된다.")
    void interceptorsPreProcess() {
        request.setAttribute(TEST, new ArrayList<Integer>());
        interceptors.preProcess(request, response);

        assertThat(request.getAttribute(TEST)).isEqualTo(Arrays.asList(1, 3));
    }

    private static class TestInterceptor1 implements Interceptor {
        @Override
        public void preProcess(HttpServletRequest request, HttpServletResponse response) {
            List<Integer> ints = (List<Integer>) request.getAttribute(TEST);
            ints.add(1);
        }

        @Override
        public void postProcess(HttpServletRequest request, HttpServletResponse response) {
            List<Integer> ints = (List<Integer>) request.getAttribute(TEST);
            ints.add(2);
        }
    }

    private static class TestInterceptor2 implements Interceptor {

        @Override
        public void preProcess(HttpServletRequest request, HttpServletResponse response) {
            List<Integer> ints = (List<Integer>) request.getAttribute(TEST);
            ints.add(3);
        }

        @Override
        public void postProcess(HttpServletRequest request, HttpServletResponse response) {
            List<Integer> ints = (List<Integer>) request.getAttribute(TEST);
            ints.add(4);
        }
    }
}

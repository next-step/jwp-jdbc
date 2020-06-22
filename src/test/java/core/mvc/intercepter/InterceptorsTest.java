package core.mvc.intercepter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("인터셉터들 테스트")
class InterceptorsTest {
    private final List<Interceptor> interceptors = Arrays.asList(new TestInterceptor1(), new TestInterceptor2());

    @Test
    @DisplayName("interceptors 는 여러 인터셉터를 가지고 초기화 할 수 있다.")
    void init() {
        assertThatCode(() -> new Interceptors(this.interceptors))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("interceptors 를 초기화 할때 null 을 넘기면 예외가 발생한다")
    void initFail() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Interceptors(null));
    }




    private static class TestInterceptor1 implements Interceptor {
        @Override
        public void preProcess(HttpServletRequest request, HttpServletResponse response) {

        }

        @Override
        public void postProcess(HttpServletRequest request, HttpServletResponse response) {

        }
    }

    private static class TestInterceptor2 implements Interceptor {

        @Override
        public void preProcess(HttpServletRequest request, HttpServletResponse response) {

        }

        @Override
        public void postProcess(HttpServletRequest request, HttpServletResponse response) {

        }
    }
}

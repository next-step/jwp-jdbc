package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author KingCjy
 */
public class InterceptorTest {

    private HandlerInterceptor interceptor;
    private AtomicLong start;
    private AtomicLong end;

    @BeforeEach
    public void setUp() {
        start = new AtomicLong();
        end = new AtomicLong();
        interceptor = new HandlerInterceptorComposite(new TestHandlerInterceptor(start, end));
    }

    @Test
    public void processTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response);
        interceptor.postHandler(request, response);

        assertThat(start.get()).isNotNull();
        assertThat(end.get()).isNotNull();
    }

    static class TestHandlerInterceptor implements HandlerInterceptor {

        private Logger logger = LoggerFactory.getLogger(TestHandlerInterceptor.class);

        private AtomicLong start;
        private AtomicLong end;

        public TestHandlerInterceptor(AtomicLong start, AtomicLong end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void preHandle(HttpServletRequest request, HttpServletResponse response) {
            start.set(System.currentTimeMillis());
            logger.info("preHandle");
        }

        @Override
        public void postHandler(HttpServletRequest request, HttpServletResponse response) {
            end.set(System.currentTimeMillis());
            logger.info("postHandle : {} ms", end.get() - start.get());
        }
    }
}

package core.mvc;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

import static core.mvc.TimeLoggingInterceptor.THREAD_LOGIC_PROCEED_TIME_MS;
import static org.assertj.core.api.Assertions.assertThat;

class TimeLoggingInterceptorTest {
    private static final Logger logger = LoggerFactory.getLogger(TimeLoggingInterceptorTest.class);


    private HttpServletRequest req = new MockHttpServletRequest();
    private HttpServletResponse resq = new MockHttpServletResponse();
    private Interceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new TimeLoggingInterceptor();
    }

    @DisplayName("preHandle은 현재 시간을 출력하며, 시작시간을 스레드로컬에 저장한다.")
    @Test
    void preHandle() throws Exception {
        interceptor.preHandle(req, resq, null);

        ThreadLocal<LocalDateTime> timeLogger = getTimeLogger();

        assertThat(timeLogger.get()).isNotNull();
    }

    @DisplayName("postHandle은 preHandle로부터 지난 시간만큼의 차이를 출력한다.")
    @Test
    void postHandle() throws Exception {
        ListAppender<ILoggingEvent> appender = LoggerTestUtil.getListAppenderForClass(TimeLoggingInterceptor.class);
        interceptor.preHandle(req, resq, null);
        Thread.sleep(1000L);
        interceptor.postHandle(req, resq, null, null);

        List<ILoggingEvent> list = appender.list;
        assertThat(list).isNotEmpty();
        assertThat(list.get(list.size() - 1).getMessage()).isEqualTo(THREAD_LOGIC_PROCEED_TIME_MS);
    }

    @DisplayName("afterCompletion 은 스레드로컬에서 시간정보를 제거하고, 예외가 있을 경우 로그 에러레벨로 출력한다. ")
    @Test
    void afterCompletion() throws Exception {
        ListAppender<ILoggingEvent> appender = LoggerTestUtil.getListAppenderForClass(TimeLoggingInterceptor.class);
        String testMessage = "Test Message";
        interceptor.preHandle(req, resq, null);
        Thread.sleep(1000L);
        interceptor.postHandle(req, resq, null, null);

        interceptor.afterCompletion(req, resq, null, new RuntimeException(testMessage));

        List<ILoggingEvent> list = appender.list;
        assertThat(list.get(list.size()-1).getMessage()).isEqualTo(testMessage);
    }


    private ThreadLocal<LocalDateTime> getTimeLogger() {
        return (ThreadLocal<LocalDateTime>) ReflectionTestUtils.getField(interceptor, "timeLogger");
    }
}

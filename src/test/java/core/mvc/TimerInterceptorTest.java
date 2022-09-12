package core.mvc;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("각 Controller 메서드의 실행 속도 측정 인터셉터")
class TimerInterceptorTest {

    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    private TimerInterceptor interceptor;
    private ListAppender<ILoggingEvent> logEvents;

    @BeforeEach
    void before() {
        interceptor = new TimerInterceptor();
        logEvents = logEvents();
    }

    @DisplayName("Controller 메서드 속도 측정 성공")
    @Test
    void time_stop_watch() {
        interceptor.preHandle(request, response, new Object());
        interceptor.postHandle(request, response, new Object(), new ModelAndView());
        interceptor.afterCompletion(request, response, new Object(), new Exception());

        ILoggingEvent logger = logEvents.list.get(0);
        assertAll(
                () -> assertThat(logger.getLevel()).isEqualTo(Level.DEBUG),
                () -> assertThat(logger.getMessage()).contains("process")
        );
    }

    @DisplayName("인터셉터는 preHandle 메서드부터 호출해야한다")
    @Test
    void call_preHandle_first() {
        interceptor.postHandle(request, response, new Object(), new ModelAndView());

        ILoggingEvent logger = logEvents.list.get(0);
        assertAll(
                () -> assertThat(logger.getLevel()).isEqualTo(Level.ERROR),
                () -> assertThat(logger.getMessage()).contains("preHandle")
        );
    }

    private ListAppender<ILoggingEvent> logEvents() {
        Logger logger = (Logger) LoggerFactory.getLogger(TimerInterceptor.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        return listAppender;
    }
}

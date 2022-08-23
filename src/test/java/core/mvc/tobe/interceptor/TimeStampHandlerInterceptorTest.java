package core.mvc.tobe.interceptor;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("시간 스탬프 인터셉터")
class TimeStampHandlerInterceptorTest {

    private static final MockHttpServletRequest ANY_REQUEST = new MockHttpServletRequest();
    private static final MockHttpServletResponse ANY_RESPONSE = new MockHttpServletResponse();

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
                .isThrownBy(TimeStampHandlerInterceptor::new);
    }

    @Test
    @DisplayName("소요 시간 로그 찍힘")
    void timeLog() {
        //given
        TimeStampHandlerInterceptor interceptor = new TimeStampHandlerInterceptor();
        ListAppender<ILoggingEvent> logEvents = logEvents();
        //when
        interceptor.preHandle(ANY_REQUEST, ANY_RESPONSE);
        interceptor.postHandle(ANY_REQUEST, ANY_RESPONSE);
        interceptor.afterCompletion(ANY_REQUEST, ANY_RESPONSE);
        //then
        ILoggingEvent log = logEvents.list.get(0);
        assertAll(
                () -> assertThat(log.getLevel()).isEqualTo(Level.DEBUG),
                () -> assertThat(log.getMessage()).contains("process")
        );
    }

    @Test
    @DisplayName("반드시 preHandle 부터 호출")
    void timeLog_exceptPreHandle_logError() {
        //given
        TimeStampHandlerInterceptor interceptor = new TimeStampHandlerInterceptor();
        ListAppender<ILoggingEvent> logEvents = logEvents();
        //when
        interceptor.postHandle(ANY_REQUEST, ANY_RESPONSE);
        //then
        ILoggingEvent log = logEvents.list.get(0);
        assertAll(
                () -> assertThat(log.getLevel()).isEqualTo(Level.ERROR),
                () -> assertThat(log.getMessage()).contains("preHandle")
        );
    }

    private ListAppender<ILoggingEvent> logEvents() {
        Logger logger = (Logger) LoggerFactory.getLogger(TimeStampHandlerInterceptor.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        return listAppender;
    }
}

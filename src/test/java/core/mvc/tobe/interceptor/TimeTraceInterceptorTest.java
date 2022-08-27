package core.mvc.tobe.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import core.di.factory.example.QnaController;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class TimeTraceInterceptorTest {

    @DisplayName("수행 시간을 출력한다")
    @Test
    void time_trace() throws Exception {
        final Logger logger = (Logger) LoggerFactory.getLogger(TimeTraceInterceptor.class);

        final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        logger.addAppender(listAppender);

        final TimeTraceInterceptor timeTraceInterceptor = new TimeTraceInterceptor();

        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final QnaController qnaController = new QnaController(null);

        // when
        timeTraceInterceptor.preHandle(request, response, qnaController);

        sleep();

        timeTraceInterceptor.afterCompletion(request, response, qnaController);

        // then
        final List<ILoggingEvent> loggingEvents = listAppender.list;
        final ILoggingEvent startTImeActual = loggingEvents.get(0);
        final ILoggingEvent endTimeActual = loggingEvents.get(1);
        final ILoggingEvent durationActual = loggingEvents.get(2);

        assertAll(
            () -> assertThat(startTImeActual.getLevel()).isSameAs(Level.DEBUG),
            () -> assertThat(endTimeActual.getLevel()).isSameAs(Level.DEBUG),
            () -> assertThat(durationActual.getLevel()).isSameAs(Level.DEBUG),

            () -> assertThat(startTImeActual.getFormattedMessage()).startsWith("core.di.factory.example.QnaController 의 시작 시간:"),
            () -> assertThat(endTimeActual.getFormattedMessage()).startsWith("core.di.factory.example.QnaController 의 종료 시간:"),
            () -> assertThat(durationActual.getFormattedMessage()).startsWith("core.di.factory.example.QnaController 의 수행 시간: 15")
        );
    }

    private static void sleep() {
        try {
            Thread.sleep(1_500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

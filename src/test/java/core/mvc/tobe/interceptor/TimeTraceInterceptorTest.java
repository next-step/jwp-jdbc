package core.mvc.tobe.interceptor;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import core.di.factory.example.QnaController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class TimeTraceInterceptorTest {

    @DisplayName("수행 시간을 출력한다")
    @Test
    void time_trace() {
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

        timeTraceInterceptor.postHandle(request, response, qnaController);

        // then
        final ILoggingEvent actual = listAppender.list.get(0);

        assertThat(actual.getLevel()).isSameAs(Level.DEBUG);
        assertThat(actual.getFormattedMessage()).contains("core.di.factory.example.QnaController 의 수행 시간: 1.5");
    }

    private static void sleep() {
        try {
            Thread.sleep(1_500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

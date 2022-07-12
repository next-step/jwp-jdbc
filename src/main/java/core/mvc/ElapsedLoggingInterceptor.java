package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ElapsedLoggingInterceptor implements Interceptor {

    private static final String START_TIME_ATTRIBUTE = "startTime";

    private static final Logger logger = LoggerFactory.getLogger(ElapsedLoggingInterceptor.class);

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        LocalDateTime start = LocalDateTime.now();
        request.setAttribute(START_TIME_ATTRIBUTE, start);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        LocalDateTime startTime = (LocalDateTime) request.getAttribute(START_TIME_ATTRIBUTE);
        String elapsed = String.valueOf(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS));

        logger.debug("elapsed: {}", elapsed);
    }
}

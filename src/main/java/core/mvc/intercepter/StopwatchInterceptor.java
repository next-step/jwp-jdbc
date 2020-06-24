package core.mvc.intercepter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;

public class StopwatchInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(StopwatchInterceptor.class);
    private static final String TIME = "stop_watch_time";

    @Override
    public void preProcess(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(TIME, Instant.now());
    }

    @Override
    public void postProcess(HttpServletRequest request, HttpServletResponse response) {
        Instant end = Instant.now();
        Instant start = (Instant) request.getAttribute(TIME);

        logger.debug("Request process time : {} ms", Duration.between(start, end).toMillis());
    }
}

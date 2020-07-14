package next.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

/**
 * Created By kjs4395 on 7/13/20
 */
public class StopWatchInterceptor implements Interceptor{
    private static final Logger logger = LoggerFactory.getLogger(StopWatchInterceptor.class);
    private String stopwatchAttr = "stopWatch";

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(stopwatchAttr, LocalDateTime.now());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        LocalDateTime endTime = LocalDateTime.now();
        request.getAttribute(stopwatchAttr);
        long executeTime = Duration.between((Temporal) request.getAttribute(stopwatchAttr),endTime).toMillis();
        logger.debug("execute time : " + executeTime);
    }
}

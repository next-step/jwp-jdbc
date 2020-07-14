package core.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by iltaek on 2020/07/07 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class StopWatchInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(StopWatchInterceptor.class);

    private final ThreadLocal<Long> startedTime = new ThreadLocal<>();

    @Override
    public void preHandle(HttpServletRequest req, HttpServletResponse resp) {
        startedTime.remove();
        startedTime.set(System.nanoTime());
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp) {
        Long elapsedTime = System.nanoTime() - startedTime.get();
        logger.debug("Elapsed time to handle {} Request is {} ns", req.getRequestURI(), elapsedTime);
    }
}

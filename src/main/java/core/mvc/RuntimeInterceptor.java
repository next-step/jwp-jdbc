package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RuntimeInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(RuntimeInterceptor.class);

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        long endTime = System.currentTimeMillis();
        long startTime = (long )request.getAttribute("startTime");

        long processTime = endTime - startTime;
        logger.debug("Request Process Time : {} ms", processTime);
    }
}

package core.interceptor;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RunningTimeInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RunningTimeInterceptor.class);

    private long startAt;
    private long endAt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        this.startAt = System.currentTimeMillis();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav) throws Exception {
        this.endAt = System.currentTimeMillis();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) throws Exception {
        long runningTime = this.endAt - this.startAt;
        String name;
        if (obj instanceof Controller) {
            name = ((Controller) obj).getClass().getSimpleName();
        } else if (obj instanceof HandlerExecution) {
            name = ((HandlerExecution) obj).getMethod().getName();
        } else {
            throw new ServletException("Invalid handler");
        }

        logger.debug(name + " running time : " + runningTime + "ms");
    }
}

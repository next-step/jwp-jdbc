package core.mvc.interceptor;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicInterceptor implements Interceptor{
    private static final Logger logger = LoggerFactory.getLogger(BasicInterceptor.class);

    @Override
    public boolean pre(final HttpServletRequest request, final HttpServletResponse response, final Object object) {
        if (object instanceof Controller) {
            request.setAttribute("begin", System.currentTimeMillis());
            return true;
        }
        return false;
    }

    @Override
    public void post(final HttpServletRequest request, final HttpServletResponse response, final Object object, final ModelAndView modelAndView) {
        if (pre(request, response, object)) {
            long finish = System.currentTimeMillis();
            long begin = (long) request.getAttribute("begin");
            logger.debug("메서드 실행 시간 : " + (finish - begin));
        }
    }
}

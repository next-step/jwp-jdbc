package core.mvc.tobe.interceptor;

import core.mvc.ModelAndView;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestMappedInterceptor extends MappedInterceptor {
    private static final Logger log = LoggerFactory.getLogger(TestMappedInterceptor.class);

    public TestMappedInterceptor() {
        super(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public TestMappedInterceptor(String... matchingUris) {
        super(matchingUris);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("TestMappedInterceptor - preHandle()");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.debug("TestMappedInterceptor - postHandle()");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.debug("TestMappedInterceptor - afterCompletion()");
    }
}

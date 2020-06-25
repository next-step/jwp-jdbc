package core.mvc.tobe.interceptor;

import core.mvc.ModelAndView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
public class UserFormInterceptor extends MappedInterceptor {
    public UserFormInterceptor(String ...matchingUris) {
        super(matchingUris);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("UserFormInterceptor - preHandle()");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("UserFormInterceptor - postHandle()");
    }
}

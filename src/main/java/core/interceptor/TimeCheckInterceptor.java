package core.interceptor;

import core.mvc.HandlerInterceptor;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TimeCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

    }
}

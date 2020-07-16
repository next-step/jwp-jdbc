package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterceptor {

    boolean preHandle(HttpServletRequest request, HttpServletResponse response);

    void postHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView);

}

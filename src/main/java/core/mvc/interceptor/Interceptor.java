package core.mvc.interceptor;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Interceptor {

    boolean pre(HttpServletRequest request, HttpServletResponse response, Object object);

    void post(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView);
}

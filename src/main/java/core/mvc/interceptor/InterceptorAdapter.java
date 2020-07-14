package core.mvc.interceptor;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class InterceptorAdapter implements Interceptor {

    @Override
    public boolean pre(final HttpServletRequest request, final HttpServletResponse response, final Object object) {
        return false;
    }

    @Override
    public void post(final HttpServletRequest request, final HttpServletResponse response, final Object object, final ModelAndView modelAndView) {
    }
}

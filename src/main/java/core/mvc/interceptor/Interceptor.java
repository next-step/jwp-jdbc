package core.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Interceptor {
    void preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}

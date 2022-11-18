package core.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Interceptor {
    boolean preHandle(HttpServletRequest request, HttpServletResponse response);

    void postHandle(HttpServletRequest request, HttpServletResponse response);
}

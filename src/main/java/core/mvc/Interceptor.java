package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Interceptor {
    void preHandle(HttpServletRequest request, HttpServletResponse response);

    void postHandle(HttpServletRequest request, HttpServletResponse response);
}

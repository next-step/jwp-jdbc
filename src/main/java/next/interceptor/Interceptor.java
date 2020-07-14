package next.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created By kjs4395 on 7/13/20
 */
public interface Interceptor {
    void preHandle(HttpServletRequest request, HttpServletResponse response);
    void postHandle(HttpServletRequest request, HttpServletResponse response);
}

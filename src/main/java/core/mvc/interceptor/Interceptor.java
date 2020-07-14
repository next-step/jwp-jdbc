package core.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by iltaek on 2020/07/07 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public interface Interceptor {

    void preHandle(HttpServletRequest req, HttpServletResponse resp);
    void postHandle(HttpServletRequest req, HttpServletResponse resp);
}

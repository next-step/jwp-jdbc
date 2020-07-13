package core.mvc;

import core.mvc.interceptor.Interceptor;
import java.util.Stack;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by iltaek on 2020/07/07 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class HandlerInterceptorRegistry {

    private static final Stack<Interceptor> preInterceptors = new Stack<>();
    private static final Stack<Interceptor> postInterceptors = new Stack<>();

    public void addInterceptor(Interceptor interceptor) {
        preInterceptors.push(interceptor);
    }

    public void preHandle(HttpServletRequest req, HttpServletResponse resp) {
        for (int i = 0; i < preInterceptors.size(); i++) {
            Interceptor interceptor = preInterceptors.pop();
            interceptor.preHandle(req, resp);
            postInterceptors.push(interceptor);
        }
    }

    public void postHandle(HttpServletRequest req, HttpServletResponse resp) {
        for (int i = 0; i < postInterceptors.size(); i++) {
            Interceptor interceptor = postInterceptors.pop();
            interceptor.postHandle(req, resp);
            preInterceptors.push(interceptor);
        }
    }
}

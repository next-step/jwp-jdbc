package core.mvc;

import core.mvc.interceptor.Interceptor;
import java.util.Stack;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by iltaek on 2020/07/07 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class HandlerInterceptorRegistry {

    private static final ThreadLocal<Stack<Interceptor>> preInterceptors = new ThreadLocal<>();
    private static final ThreadLocal<Stack<Interceptor>> postInterceptors = new ThreadLocal<>();

    public void initialize() {
        preInterceptors.remove();
        postInterceptors.remove();

        preInterceptors.set(new Stack<>());
        postInterceptors.set(new Stack<>());
    }

    public void addInterceptor(Interceptor interceptor) {
        preInterceptors.get().push(interceptor);
    }

    public void preHandle(HttpServletRequest req, HttpServletResponse resp) {
        for (int i = 0; i < preInterceptors.get().size(); i++) {
            Interceptor interceptor = preInterceptors.get().pop();
            interceptor.preHandle(req, resp);
            postInterceptors.get().push(interceptor);
        }
    }

    public void postHandle(HttpServletRequest req, HttpServletResponse resp) {
        for (int i = 0; i < postInterceptors.get().size(); i++) {
            Interceptor interceptor = postInterceptors.get().pop();
            interceptor.postHandle(req, resp);
            preInterceptors.get().push(interceptor);
        }
    }
}

package core.mvc;

import com.google.common.collect.Lists;
import core.mvc.interceptor.Interceptor;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by iltaek on 2020/07/07 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class HandlerInterceptorRegistry {

    private static final List<Interceptor> interceptors = Lists.newArrayList();

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public void preHandle(HttpServletRequest req, HttpServletResponse resp) {
        interceptors.forEach(interceptor -> interceptor.preHandle(req, resp));
    }

    public void postHandle(HttpServletRequest req, HttpServletResponse resp) {
        interceptors.forEach(interceptor -> interceptor.postHandle(req, resp));
    }
}

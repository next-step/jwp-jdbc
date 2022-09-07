package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class InterceptorRegistry {

    private final List<Interceptor> interceptors = new ArrayList<>();

    public void addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    public void preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.interceptors
                .forEach(interceptor -> interceptor.preHandle(httpServletRequest, httpServletResponse));
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.interceptors
                .forEach(interceptor -> interceptor.postHandle(httpServletRequest, httpServletResponse));
    }

}

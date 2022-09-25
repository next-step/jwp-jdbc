package core.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class InterceptorRegistry implements Interceptor {

    private final List<Interceptor> interceptors = new ArrayList<>();

    @Override
    public void preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        interceptors.forEach(interceptor -> interceptor.preHandle(httpServletRequest, httpServletResponse));
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        for (int i = interceptors.size() -1; i>=0; i--) {
            interceptors.get(i).postHandle(httpServletRequest, httpServletResponse);
        }
    }

    public void addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
    }
}

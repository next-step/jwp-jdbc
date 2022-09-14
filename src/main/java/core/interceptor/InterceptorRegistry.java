package core.interceptor;

import core.interceptor.Interceptor;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InterceptorRegistry {
    private final List<Interceptor> interceptors = new ArrayList<>();

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        interceptors.forEach(interceptor -> interceptor.preHandle(request, response));
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        for (int i = interceptors.size() - 1; i >= 0; i--){
            interceptors.get(i).postHandle(request, response);
        }
    }

}

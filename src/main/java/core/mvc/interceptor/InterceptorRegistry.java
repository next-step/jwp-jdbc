package core.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class InterceptorRegistry {
    private final List<Interceptor> interceptors = new ArrayList<>();

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {
        return interceptors.stream()
                .allMatch(interceptor -> interceptor.preHandle(request, response));
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        interceptors.forEach(interceptor -> interceptor.postHandle(request, response));
//        for (int i = interceptors.size() - 1; i >= 0; i--) {
//            interceptors.get(i).postHandle(request, response);
//        }
    }

    public void addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
    }
}

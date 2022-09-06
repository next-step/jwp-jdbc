package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class InterceptorRegistry {
    private List<Interceptor> interceptors = new ArrayList<>();

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        for (int i = 0; i < interceptors.size(); i++) {
             interceptors.get(i).preHandle(request, response);
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).postHandle(request, response);
        }
    }
}

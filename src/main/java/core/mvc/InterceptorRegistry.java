package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InterceptorRegistry implements Interceptor {

    List<Interceptor> interceptors = new ArrayList<>();

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        interceptors.forEach(it -> it.preHandle(request, response));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        interceptors.stream()
                .sorted(Collections.reverseOrder())
                .forEach(it -> it.postHandle(request, response));
    }
}

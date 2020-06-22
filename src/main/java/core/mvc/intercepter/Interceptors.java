package core.mvc.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Interceptors {
    private final List<Interceptor> interceptors = new ArrayList<>();

    public Interceptors(final Collection<Interceptor> interceptors) {
        if (interceptors == null) {
            throw new IllegalArgumentException("Interceptors can't be a null");
        }

        this.interceptors.addAll(interceptors);
    }

    public void preProcess(HttpServletRequest request, HttpServletResponse response) {
        interceptors.forEach(interceptor -> interceptor.preProcess(request, response));
    }
}

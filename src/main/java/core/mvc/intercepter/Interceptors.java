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

    public void postProcess(HttpServletRequest request, HttpServletResponse response) {
        for (int i = interceptors.size() - 1 ; i >= 0 ; --i) { //reverse order using stream not tidy
            interceptors.get(i).postProcess(request, response);
        }
    }
}

package core.mvc.interceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InterceptorRegistry {

    private final List<HandlerInterceptor> interceptors = new ArrayList<>();

    public void addInterceptor(HandlerInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public List<HandlerInterceptor> get() {
        return List.copyOf(interceptors);
    }

    public List<HandlerInterceptor> getReverseOrder() {
        return interceptors.stream()
            .sorted(Collections.reverseOrder())
            .collect(Collectors.toList());
    }
}

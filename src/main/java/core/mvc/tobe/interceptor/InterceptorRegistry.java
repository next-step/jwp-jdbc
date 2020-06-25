package core.mvc.tobe.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InterceptorRegistry {
    private final List<HandlerInterceptor> interceptors = new ArrayList<>();

    public InterceptorRegistry addInterceptor(HandlerInterceptor interceptor) {
        this.interceptors.add(interceptor);
        return this;
    }

    public List<HandlerInterceptor> getMatchedInterceptors(String requestUri) {
        return this.interceptors.stream()
            .filter(interceptor -> interceptor.matches(requestUri))
            .collect(Collectors.toList());
    }
}

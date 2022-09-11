package core.mvc.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HandlerInterceptorRegistry {
    private final List<InterceptorRegistration> registrations = new ArrayList<>();

    public InterceptorRegistration addInterceptor(HandlerInterceptor interceptor) {
        InterceptorRegistration registration = new InterceptorRegistration(interceptor);
        this.registrations.add(registration);
        return registration;
    }

    public List<HandlerInterceptor> getMatchedInterceptors(String url) {
        return this.registrations.stream()
                .map(registration -> registration.getMatchedInterceptor(url).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}

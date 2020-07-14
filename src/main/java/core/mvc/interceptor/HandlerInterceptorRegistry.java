package core.mvc.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerInterceptorRegistry {

    private List<InterceptorRegistration> registrations = new ArrayList<>();

    public InterceptorRegistration addInterceptor(HandlerInterceptor handlerInterceptor) {
        InterceptorRegistration interceptorRegistration = new InterceptorRegistration(handlerInterceptor);
        registrations.add(interceptorRegistration);

        return interceptorRegistration;
    }

    public HandlerInterceptors findInterceptors(String uri) {
        List<HandlerInterceptor> interceptors = this.registrations.stream()
                .filter(registration -> registration.supportsPattern(uri))
                .map(InterceptorRegistration::getHandlerInterceptor)
                .collect(Collectors.toList());

        return new HandlerInterceptors(interceptors);
    }

}

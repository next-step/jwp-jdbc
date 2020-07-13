package core.mvc.interceptor;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerInterceptorRegistry {

    private static final PathMatcher DEFAULT_PATH_MATCHER = new AntPathMatcher();

    private List<InterceptorRegistration> interceptors = new ArrayList<>();

    public InterceptorRegistration addInterceptor(HandlerInterceptor handlerInterceptor) {
        InterceptorRegistration interceptorRegistration = new InterceptorRegistration(handlerInterceptor, DEFAULT_PATH_MATCHER);
        interceptors.add(interceptorRegistration);

        return interceptorRegistration;
    }

    public List<HandlerInterceptor> findInterceptors(String uri) {
        return interceptors.stream()
                .map(InterceptorRegistration::getHandlerInterceptor)
                .collect(Collectors.toList());
        // TODO: 2020/07/13 filter twice
    }

}

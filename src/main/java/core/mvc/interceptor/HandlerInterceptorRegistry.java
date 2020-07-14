package core.mvc.interceptor;

import core.annotation.Configuration;
import core.mvc.config.WebMvcConfigurer;
import core.util.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerInterceptorRegistry {

    private static final String INTERCEPTOR_ADD_METHOD_NAME = "addInterceptors";

    private List<InterceptorRegistration> registrations = new ArrayList<>();

    public HandlerInterceptorRegistry(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Class<?> webMvcConfigurerClass = findWebMvcConfigurer(reflections);

        try {
            if (webMvcConfigurerClass != null) {
                Method addInterceptorsMethod = webMvcConfigurerClass.getMethod(INTERCEPTOR_ADD_METHOD_NAME, this.getClass());

                Object instance = ReflectionUtils.newInstance(webMvcConfigurerClass);
                addInterceptorsMethod.invoke(instance, this);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("InterceptorRegistry initialize failed.", e);
        }
    }

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

    private Class<?> findWebMvcConfigurer(Reflections reflections) {
        return reflections.getTypesAnnotatedWith(Configuration.class).stream()
                .filter(WebMvcConfigurer.class::isAssignableFrom)
                .findFirst()
                .orElse(null);
    }

}

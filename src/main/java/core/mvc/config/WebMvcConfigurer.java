package core.mvc.config;

import core.mvc.interceptor.HandlerInterceptorRegistry;

public interface WebMvcConfigurer {

    default void addInterceptors(HandlerInterceptorRegistry registry) {
    }

}

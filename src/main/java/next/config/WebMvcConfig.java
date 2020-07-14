package next.config;

import core.annotation.Configuration;
import core.mvc.config.WebMvcConfigurer;
import core.mvc.interceptor.HandlerInterceptorRegistry;
import next.config.interceptor.ExecutionTimerInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(HandlerInterceptorRegistry registry) {
        registry.addInterceptor(new ExecutionTimerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/users/loginForm"); // 적용 예시
    }

}

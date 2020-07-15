package core.mvc;

import core.interceptor.TimeCheckInterceptor;
import org.junit.jupiter.api.Test;

public class HandlerInterceptorRegistryTest {

    @Test
    public void initInterceptor() {
        HandlerInterceptorRegistry registry = new HandlerInterceptorRegistry();
        registry.addHandlerInterceptor(new TimeCheckInterceptor());
    }
}

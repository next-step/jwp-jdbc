package core.mvc;

import core.web.interceptor.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;

public final class InterceptorRegistry {

    private final List<HandlerInterceptor> registrations = new ArrayList<>();

    /**
     * @param interceptor interceptor to add
     */
    public void addInterceptor(HandlerInterceptor interceptor) {
        registrations.add(interceptor);
    }

    public List<HandlerInterceptor> getInterceptors() {
        return registrations;
    }
}

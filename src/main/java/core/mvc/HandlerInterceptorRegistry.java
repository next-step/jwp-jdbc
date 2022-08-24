package core.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerInterceptorRegistry {

    private final List<HandlerInterceptor> interceptors = new ArrayList<>();

    public void addInterceptor(HandlerInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public boolean hasInterceptor() {
        return !interceptors.isEmpty();
    }
}

package core.mvc.intercepter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Interceptors {
    private final List<Interceptor> interceptors = new ArrayList<>();

    public Interceptors(final Collection<Interceptor> interceptors) {
        if (interceptors == null) {
            throw new IllegalArgumentException("Interceptors can't be a null");
        }

        this.interceptors.addAll(interceptors);
    }
}

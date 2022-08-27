package core.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HandlerInterceptorExecution {

    private final HandlerInterceptor handlerInterceptor;
    private final List<String> pathPatterns = new ArrayList<>();

    public HandlerInterceptorExecution(final HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
    }

    public void addPathPatterns(String... pathPatterns) {
        this.pathPatterns.addAll(Arrays.asList(pathPatterns));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HandlerInterceptorExecution that = (HandlerInterceptorExecution) o;
        return Objects.equals(handlerInterceptor, that.handlerInterceptor) && Objects.equals(pathPatterns, that.pathPatterns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handlerInterceptor, pathPatterns);
    }
}

package core.mvc;

import core.util.PathPatternUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerInterceptorExecution {

    private final HandlerInterceptor handlerInterceptor;
    private final List<String> pathPatterns = new ArrayList<>();

    public HandlerInterceptorExecution(final HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
    }

    public HandlerInterceptorExecution(final HandlerInterceptor handlerInterceptor, final String... pathPatterns) {
        this.handlerInterceptor = handlerInterceptor;
        this.pathPatterns.addAll(Arrays.asList(pathPatterns));
    }

    public void addPathPatterns(String... pathPatterns) {
        this.pathPatterns.addAll(Arrays.asList(pathPatterns));
    }

    public Optional<HandlerInterceptor> getHandlerInterceptor(String url) {
        if (pathPatterns.isEmpty()) {
            return Optional.of(this.handlerInterceptor);
        }

        return getHandlerInterceptorInternal(url);
    }

    private Optional<HandlerInterceptor> getHandlerInterceptorInternal(final String url) {
        if (isUrlMatch(url)) {
            return Optional.of(this.handlerInterceptor);
        }

        return Optional.empty();
    }

    private boolean isUrlMatch(final String url) {
        return pathPatterns.stream()
            .anyMatch(pattern -> PathPatternUtil.isUrlMatch(pattern, url));
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

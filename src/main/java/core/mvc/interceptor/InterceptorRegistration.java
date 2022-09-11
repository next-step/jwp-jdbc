package core.mvc.interceptor;

import core.util.PathPatternUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InterceptorRegistration {
    private final HandlerInterceptor interceptor;

    private List<String> includePatterns = new ArrayList<>();

    private List<String> excludePatterns = new ArrayList<>();

    public InterceptorRegistration(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public InterceptorRegistration addPathPatterns(String... patterns) {
        return addPathPatterns(Arrays.asList(patterns));
    }

    public InterceptorRegistration addPathPatterns(List<String> patterns) {
        this.includePatterns = this.includePatterns != null ? this.includePatterns : new ArrayList<>(patterns.size());
        this.includePatterns.addAll(patterns);
        return this;
    }

    public InterceptorRegistration excludePathPatterns(String... patterns) {
        return excludePathPatterns(Arrays.asList(patterns));
    }

    public InterceptorRegistration excludePathPatterns(List<String> patterns) {
        this.excludePatterns = this.excludePatterns != null ? this.excludePatterns : new ArrayList<>(patterns.size());
        this.excludePatterns.addAll(patterns);
        return this;
    }

    public Optional<HandlerInterceptor> getMatchedInterceptor(String url) {
        if (includePatternsMatch(url) && !excludePatternsMatch(url)) {
            return Optional.of(this.interceptor);
        }
        return Optional.empty();
    }

    private boolean includePatternsMatch(String pathPattern) {
        return this.includePatterns.stream()
                .anyMatch(includePattern -> PathPatternUtil.isUrlMatch(includePattern, pathPattern));
    }

    private boolean excludePatternsMatch(String pathPattern) {
        return this.excludePatterns.stream()
                .anyMatch(excludePattern -> PathPatternUtil.isUrlMatch(excludePattern, pathPattern));
    }
}

package core.mvc.interceptor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class InterceptorRegistration {

    private final HandlerInterceptor handlerInterceptor;
    private PathMatcher pathMatcher;
    private List<String> includePattern = new ArrayList<>();
    private List<String> excludePattern = new ArrayList<>();

    public InterceptorRegistration(HandlerInterceptor handlerInterceptor, PathMatcher pathMatcher) {
        this.handlerInterceptor = handlerInterceptor;
        this.pathMatcher = pathMatcher;
    }

    public InterceptorRegistration addPathPatterns(String... patterns) {
        return addPathPatterns(Arrays.asList(patterns));
    }

    public InterceptorRegistration addPathPatterns(List<String> patterns) {
        this.includePattern.addAll(patterns);
        return this;
    }

    public InterceptorRegistration excludePathPatterns(String... patterns) {
        return excludePathPatterns(Arrays.asList(patterns));
    }

    public InterceptorRegistration excludePathPatterns(List<String> patterns) {
        this.excludePattern.addAll(patterns);
        return this;
    }

    public InterceptorRegistration pathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
        return this;
    }

}

package core.mvc.interceptor;

import lombok.Getter;
import org.springframework.util.PathMatcher;

import java.util.Arrays;
import java.util.List;

@Getter
public class InterceptorRegistration {

    private HandlerInterceptor handlerInterceptor;
    private InterceptorUriPatterns includePatterns;
    private InterceptorUriPatterns excludePatterns;

    public InterceptorRegistration(HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
        this.includePatterns = new InterceptorUriPatterns();
        this.excludePatterns = new InterceptorUriPatterns();
    }

    public InterceptorRegistration addPathPatterns(String... patterns) {
        return addPathPatterns(Arrays.asList(patterns));
    }

    public InterceptorRegistration addPathPatterns(List<String> patterns) {
        this.includePatterns.addAll(patterns);
        return this;
    }

    public InterceptorRegistration excludePathPatterns(String... patterns) {
        return excludePathPatterns(Arrays.asList(patterns));
    }

    public InterceptorRegistration excludePathPatterns(List<String> patterns) {
        this.excludePatterns.addAll(patterns);
        return this;
    }

    public InterceptorRegistration pathMatcher(PathMatcher pathMatcher) {
        this.includePatterns.updatePathMatcher(pathMatcher);
        this.excludePatterns.updatePathMatcher(pathMatcher);
        return this;
    }

    public boolean supportsPattern(String uri) {
        return includePatterns.matches(uri) && !excludePatterns.matches(uri);
    }

}

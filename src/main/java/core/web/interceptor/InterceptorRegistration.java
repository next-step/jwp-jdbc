package core.web.interceptor;

import java.util.Arrays;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;

public class InterceptorRegistration {

    private final HandlerInterceptor interceptor;

    private final PathMatcher pathMatcher = new AntPathMatcher();

    @Nullable
    private PatternAdapter[] includePatterns = null;

    public InterceptorRegistration(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public InterceptorRegistration addPathPatterns(String... patterns) {
        this.includePatterns = PatternAdapter.initPatterns(patterns);
        return this;
    }

    public boolean matches(String lookupPath) {
        for (PatternAdapter includePattern : includePatterns) {
            if (pathMatcher.match(includePattern.getPatternString(), lookupPath)) {
                return true;
            }
        }
        return false;
    }

    public HandlerInterceptor getInterceptor() {
        return this.interceptor;
    }

    private static class PatternAdapter {

        private final String patternString;


        public PatternAdapter(String pattern) {
            this.patternString = pattern;
        }

        public String getPatternString() {
            return this.patternString;
        }

        @Nullable
        public static PatternAdapter[] initPatterns(@Nullable String[] patterns) {
            if (ObjectUtils.isEmpty(patterns)) {
                return null;
            }

            return Arrays.stream(patterns)
                .map(PatternAdapter::new)
                .toArray(PatternAdapter[]::new);
        }
    }

}

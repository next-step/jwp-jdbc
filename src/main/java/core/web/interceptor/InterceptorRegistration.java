package core.web.interceptor;

import java.util.Arrays;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import org.springframework.web.util.pattern.PatternParseException;

public class InterceptorRegistration {

    private final HandlerInterceptor interceptor;

    private final PathMatcher pathMatcher = new AntPathMatcher();

    @Nullable
    private PatternAdapter[] includePatterns = null;

    public InterceptorRegistration(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public InterceptorRegistration addPathPatterns(String... patterns) {
        this.includePatterns = PatternAdapter.initPatterns(patterns, new PathPatternParser());
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

        @Nullable
        private final PathPattern pathPattern;


        public PatternAdapter(String pattern, @Nullable PathPatternParser parser) {
            this.patternString = pattern;
            this.pathPattern = initPathPattern(pattern, parser);
        }

        @Nullable
        private static PathPattern initPathPattern(String pattern, @Nullable PathPatternParser parser) {
            try {
                return (parser != null ? parser : PathPatternParser.defaultInstance).parse(pattern);
            }
            catch (PatternParseException ex) {
                return null;
            }
        }

        public String getPatternString() {
            return this.patternString;
        }

        @Nullable
        public static PatternAdapter[] initPatterns(
            @Nullable String[] patterns, @Nullable PathPatternParser parser) {

            if (ObjectUtils.isEmpty(patterns)) {
                return null;
            }
            return Arrays.stream(patterns)
                .map(pattern -> new PatternAdapter(pattern, parser))
                .toArray(PatternAdapter[]::new);
        }
    }

}

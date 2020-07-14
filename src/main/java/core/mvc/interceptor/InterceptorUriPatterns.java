package core.mvc.interceptor;

import core.util.PathMatcherUtil;
import lombok.NoArgsConstructor;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class InterceptorUriPatterns {

    private List<String> patterns = new ArrayList<>();
    private PathMatcher pathMatcher;

    public void addAll(List<String> patterns) {
        this.patterns.addAll(patterns);
    }

    public void updatePathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public boolean isEmpty() {
        return patterns.isEmpty();
    }

    public boolean matches(String uri) {
        if (isEmpty()) {
            return true;
        }

        return patterns.stream()
                .anyMatch(pattern -> matchesWithMatcher(pattern, uri));
    }

    private boolean matchesWithMatcher(String pattern, String uri) {
        if (this.pathMatcher == null) {
            return PathMatcherUtil.matches(pattern, uri);
        }

        return pathMatcher.match(pattern, uri);
    }

}

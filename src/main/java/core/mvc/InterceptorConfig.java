package core.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class InterceptorConfig {

    private final List<Interceptor> interceptors = new ArrayList<>();
    private final PathPatternStore includePattern = new PathPatternStore();
    private final PathPatternStore excludePattern = new PathPatternStore();

    public InterceptorConfig addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    public InterceptorConfig addPathPattern(String pathPattern) {
        includePattern.add(pathPattern);
        return this;
    }

    public InterceptorConfig excludePathPattern(String pathPattern) {
        excludePattern.add(pathPattern);
        return this;
    }

    public boolean match(String path) {
        return includePattern.match(path) && !excludePattern.match(path);
    }

    public Stream<Interceptor> getInterceptors() {
        return interceptors.stream();
    }
}

package core.mvc;

import java.util.stream.Stream;

public class DefaultInterceptorMetaData implements InterceptorMetaData {
    private final Interceptors interceptors = new Interceptors();
    private final Patterns includePatterns = new Patterns();
    private final Patterns excludePatterns = new Patterns();


    @Override
    public InterceptorMetaData addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    @Override
    public InterceptorMetaData addPathPatterns(String... path) {
        for (String value : path) {
            includePatterns.add(value);
        }
        return this;
    }

    @Override
    public InterceptorMetaData excludePathPatterns(String... path) {
        for (String value : path) {
            excludePatterns.add(value);
        }
        return this;
    }

    @Override
    public Stream<Interceptor> stream() {
        return interceptors.stream();
    }

    @Override
    public boolean matches(String path) {
        return includePatterns.matches(path) && !excludePatterns.matches(path);
    }
}

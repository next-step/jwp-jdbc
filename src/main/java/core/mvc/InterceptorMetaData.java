package core.mvc;

import java.util.stream.Stream;

public interface InterceptorMetaData {
    InterceptorMetaData addInterceptor(Interceptor interceptor);
    InterceptorMetaData addPathPatterns(String... path);
    InterceptorMetaData excludePathPatterns(String... path);

    boolean matches(String path);

    Stream<Interceptor> stream();
}

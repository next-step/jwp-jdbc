package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

@FunctionalInterface
public interface ExceptionFunction<T, R> {
    Logger logger = LoggerFactory.getLogger(ExceptionFunction.class);

    R apply(T r) throws Exception;

    static <T, R> Function<T, R> wrap(ExceptionFunction<T, R> f) {
        return (T r) -> {
            try {
                return f.apply(r);
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        };
    }
}

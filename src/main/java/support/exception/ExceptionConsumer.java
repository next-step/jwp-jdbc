package support.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

@FunctionalInterface
public interface ExceptionConsumer<T> {
    Logger logger = LoggerFactory.getLogger(ExceptionConsumer.class);

    void accept(T r) throws Exception;

    static <T> Consumer<T> wrap(ExceptionConsumer<T> f) {
        return (T r) -> {
            try {
                f.accept(r);
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        };
    }
}

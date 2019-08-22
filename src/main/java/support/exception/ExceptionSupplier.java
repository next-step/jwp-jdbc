package support.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface ExceptionSupplier<T> {
    Logger logger = LoggerFactory.getLogger(ExceptionSupplier.class);

    T get() throws Exception;

    static <T> Supplier<T> wrap(ExceptionSupplier<T> f) {
        return () -> {
            try {
                return f.get();
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        };
    }
}

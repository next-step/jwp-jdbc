package next.exception;

import java.util.function.Consumer;
import java.util.function.Function;

public class ExceptionWrapper {

    public static <T, R, E extends Exception> Function<T, R> function(FunctionWithException<T, R, E> fe) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T, E extends Exception> Consumer<T> consumer(ConsumerWithException<T, E> ce) {
        return arg -> {
            try {
                ce.accept(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}

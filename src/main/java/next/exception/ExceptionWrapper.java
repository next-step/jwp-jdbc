package next.exception;

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
}

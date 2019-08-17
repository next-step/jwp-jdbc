package core.mvc;

@FunctionalInterface
public interface ExceptionFunction<T, R> {
    R apply(T r) throws Exception;
}

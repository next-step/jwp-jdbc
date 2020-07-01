package core.mvc;

public class MethodArgumentTypeNotSupportedException extends RuntimeException {

    public MethodArgumentTypeNotSupportedException(Class<?> type, Object arg) {
        super("value [" + arg + "] does not convert [" + type + "], it does not supported");
    }
}

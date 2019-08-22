package next.jdbc;

public class DefaultConstructorNotFoundException extends ReflectionException {

    public DefaultConstructorNotFoundException(final NoSuchMethodException cause) {
        super(cause);
    }
}

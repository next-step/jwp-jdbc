package core.mvc.resolver;

import core.mvc.exception.TypeMismatchException;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractMethodArgumentResolver implements MethodArgumentResolver {

    private List<Class<?>> primitiveWrappers = Arrays.asList(Boolean.class, Integer.class, Long.class, Double.class, Float.class);

    protected Object getValueWithType(Class<?> type, String value) {

        if (int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) {
            return Integer.parseInt(value);
        }

        if (long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
            return Long.parseLong(value);
        }

        if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
            return Boolean.parseBoolean(value);
        }

        if (double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
            return Double.parseDouble(value);
        }

        if (float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
            return Float.parseFloat(value);
        }

        if (String.class.isAssignableFrom(type)) {
            return value;
        }

        throw new TypeMismatchException();
    }

    protected boolean isPrimitiveWrapperType(Class<?> type) {
        return primitiveWrappers.contains(type);
    }
}

package core.mvc.resolver;

import core.exception.TypeMisMatchException;
import core.mvc.tobe.MethodParameter;

public abstract class AbstractHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    protected Object getArgument(MethodParameter parameter, String value) throws Exception {
        if (parameter.getType().equals(int.class) || parameter.getType().equals(Integer.class)) {
            return Integer.parseInt(value);
        }

        if (parameter.getType().equals(long.class) || parameter.getType().equals(Long.class)) {
            return Long.parseLong(value);
        }

        if (parameter.getType().equals(byte.class) || parameter.getType().equals(Byte.class)) {
            return Byte.parseByte(value);
        }

        if (parameter.getType().equals(boolean.class) || parameter.getType().equals(Boolean.class)) {
            return Boolean.parseBoolean(value);
        }

        if (parameter.getType().equals(String.class)) {
            return value;
        }

        throw new TypeMisMatchException();
    }

}

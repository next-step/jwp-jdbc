package core.mvc.tobe;

import core.annotation.web.RequestBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class MethodParameter {
    private String name;
    private Class<?> type;
    private Annotation[] annotations;
    private Method method;

    private static final List<Class<?>> PRIMITIVE_TYPE;
    private static final List<Class<?>> WRAPPER_TYPE;
    static {
        PRIMITIVE_TYPE = Arrays.asList(int.class, long.class, byte.class, boolean.class);
        WRAPPER_TYPE = Arrays.asList(Integer.class, Long.class, Byte.class, Boolean.class);
    }

    public MethodParameter(String parameterName, Parameter parameter, Method method) {
        this.name = parameterName;
        this.type = parameter.getType();
        this.annotations = parameter.getAnnotations();
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isRequestBody() {
        return Arrays.stream(annotations)
                .anyMatch(annotation -> annotation.annotationType() == RequestBody.class);
    }

    public boolean isAnnotationNotExist() {
        return annotations == null || annotations.length == 0;
    }

    public boolean isJavaDataType() {
        return PRIMITIVE_TYPE.contains(type) ||
                WRAPPER_TYPE.contains(type) ||
                type.equals(String.class);
    }
}

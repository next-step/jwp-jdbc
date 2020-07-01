package core.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodParameter {

    private Method method;
    private Class<?> type;
    private Annotation[] annotations;
    private String parameterName;

    public MethodParameter(Method method, Class<?> parameterType, Annotation[] parameterAnnotation, String parameterName) {
        this.method = method;
        this.type = parameterType;
        this.annotations = parameterAnnotation;
        this.parameterName = parameterName;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getType() {
        return type;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public boolean isString() {
        return type == String.class;
    }

    public boolean isInteger() {
        return type == int.class || type == Integer.class;
    }

    public String getParameterName() {
        return parameterName;
    }
}

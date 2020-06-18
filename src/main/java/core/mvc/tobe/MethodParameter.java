package core.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author KingCjy
 */
public class MethodParameter {

    private Method method;
    private int parameterIndex;
    private String name;
    private Class<?> parameterType;

    public MethodParameter(Method method, String name, int parameterIndex) {
        this.method = method;
        this.name = name;
        this.parameterIndex = parameterIndex;
        this.parameterType = method.getParameterTypes()[this.parameterIndex];
    }

    public <T> T getParameterAnnotation(Class<T> targetAnnotation) {
        for (Annotation annotation : method.getParameters()[this.parameterIndex].getAnnotations()) {
            if (annotation.annotationType().equals(targetAnnotation)) {
                return (T) annotation;
            }
        }

        return null;
    }

    public Method getMethod() {
        return method;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public String getName() {
        return name;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }
}


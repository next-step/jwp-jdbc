package core.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodParameter {
    private String parameterName;
    private Class<?> parameterType;
    private Annotation[] annotations;
    private Method method;

    public MethodParameter(String parameterName, Class<?> type, Annotation[] annotations, Method method) {
        this.parameterName = parameterName;
        this.parameterType = type;
        this.annotations = annotations;
        this.method = method;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public Method getMethod() {
        return method;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return Arrays.stream(annotations)
                .filter(annotation::isInstance)
                .findFirst()
                .map(annotation::cast)
                .orElse(null);
    }

    public <A extends Annotation> boolean hasAnnotation(Class<A> annotation) {
        return Arrays.stream(annotations)
                .anyMatch(annotation::isInstance);
    }
}

package core.mvc.tobe.support;

import core.mvc.tobe.MethodParameter;

import java.util.Arrays;

public abstract class AbstractAnnotationArgumentResolver implements ArgumentResolver {

    protected boolean supportAnnotation(MethodParameter methodParameter, Class<?> annotation) {
        return Arrays.stream(methodParameter.getAnnotations())
                .anyMatch(ann -> ann.annotationType() == annotation);
    }

    protected <T> T getAnnotation(MethodParameter methodParameter, Class<T> annotationClazz) {
        return Arrays.stream(methodParameter.getAnnotations())
                .filter(ann -> ann.annotationType() == annotationClazz)
                .findAny()
                .map(annotationClazz::cast)
                .orElseThrow(IllegalArgumentException::new);
    }

}

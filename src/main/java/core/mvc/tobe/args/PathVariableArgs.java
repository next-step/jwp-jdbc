package core.mvc.tobe.args;

import core.annotation.web.PathVariable;
import core.exception.CoreException;
import core.exception.CoreExceptionStatus;
import core.mvc.PathVariables;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;

public class PathVariableArgs implements ArgsResolver {
    private PathVariables pathVariables;

    public PathVariableArgs(PathVariables pathVariables) {
        this.pathVariables = pathVariables;
    }

    @Override
    public boolean isArgs(Map.Entry<String, Parameter> parameterEntry) {
        Parameter parameter = parameterEntry.getValue();
        return parameter.isAnnotationPresent(PathVariable.class);
    }

    @Override
    public Object getArgs(Map.Entry<String, Parameter> parameterEntry) {
        String name = parameterEntry.getKey();
        Parameter parameter = parameterEntry.getValue();
        return getPathVariableObject(parameter, name, this.pathVariables);
    }

    private Object getPathVariableObject(Parameter parameter, String parameterName, PathVariables pathVariables) {
        PathVariable requestMapping = parameter.getAnnotation(PathVariable.class);
        if (!requestMapping.value().isEmpty()) {
            parameterName = requestMapping.value();
        }

        Object arg = pathVariables.get(parameterName, parameter.getType());
        if (requestMapping.required() && Objects.isNull(arg)) {
            throw new CoreException(CoreExceptionStatus.INVALID_PATH_VARIABLE);
        }

        return arg;
    }
}

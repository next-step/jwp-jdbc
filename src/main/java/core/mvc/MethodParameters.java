package core.mvc;

import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MethodParameters {
    private List<MethodParameter> methodParameters;

    public MethodParameters(ParameterNameDiscoverer parameterNameDiscoverer, Method method) {
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();

        if (parameterNames == null) {
            methodParameters = Collections.emptyList();
            return;
        }

        methodParameters = IntStream.range(0, parameterNames.length)
                .mapToObj(i -> new MethodParameter(parameterNames[i], parameters[i].getType(), parameters[i].getAnnotations(), method))
                .collect(Collectors.toList());
    }

    public List<MethodParameter> getMethodParameters() {
        return methodParameters;
    }
}

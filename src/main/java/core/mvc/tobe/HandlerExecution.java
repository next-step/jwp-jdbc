package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.resolver.HandlerMethodArgumentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HandlerExecution {
    private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);

    private Object declaredObject;
    private Method method;
    private List<MethodParameter> methodParameters;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
        this.methodParameters = initializeMethodParameters();
    }

    private List<MethodParameter> initializeMethodParameters() {
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

        Parameter[] parameters = this.method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);

        assert parameterNames != null;

        return IntStream.range(0, parameterNames.length)
                .mapToObj(i -> new MethodParameter(parameterNames[i], parameters[i], method))
                .collect(Collectors.toList());
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] arguments = new Object[this.methodParameters.size()];
        for (int i = 0; i < this.methodParameters.size(); i++) {
            MethodParameter methodParameter = methodParameters.get(i);

            Object argument = getResolver(methodParameter).getMethodArgument(methodParameter, request, response);
            arguments[i] = argument;
        }

        try {
            return (ModelAndView) method.invoke(declaredObject, arguments);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger.error("{} method invoke fail. error message : {}", method, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private HandlerMethodArgumentResolver getResolver(MethodParameter parameter) {
        return ResolverGenerator.getResolvers().stream()
                .filter(resolver -> resolver.supports(parameter))
                .findFirst()
                .orElse(null);
    }
}

package core.mvc.tobe;

import core.mvc.MethodParameter;
import core.mvc.MethodParameters;
import core.mvc.exception.TypeMismatchException;
import core.mvc.resolver.MethodArgumentResolver;
import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

public class HandlerExecution {

    private Object clazz;
    private Method method;
    private MethodParameters methodParameters;
    private List<MethodArgumentResolver> argumentResolvers;

    public HandlerExecution(Object clazz, Method method, MethodParameters methodParameters, List<MethodArgumentResolver> argumentResolvers) {
        this.clazz = clazz;
        this.method = method;
        this.methodParameters = methodParameters;
        this.argumentResolvers = argumentResolvers;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<MethodParameter> methodParameters = this.methodParameters.getMethodParameters();

        Object[] parameters = methodParameters.stream()
                .map(methodParameter -> resolveArgument(request, response, methodParameter))
                .toArray();

        return (ModelAndView) method.invoke(clazz, parameters);
    }

    private Object resolveArgument(HttpServletRequest request, HttpServletResponse response, MethodParameter methodParameter) {
        return argumentResolvers.stream()
                .filter(argumentResolver -> argumentResolver.supports(methodParameter))
                .findFirst()
                .map(argumentResolver -> argumentResolver.resolveArgument(methodParameter, request, response))
                .orElseThrow(TypeMismatchException::new);
    }
}

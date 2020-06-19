package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecutionImpl extends HandlerMethod implements HandlerExecution {

    private static final Logger logger = LoggerFactory.getLogger(HandlerExecutionImpl.class);

    private HandlerMethodArgumentResolver resolver = new HandlerMethodArgumentResolverComposite();

    public HandlerExecutionImpl(Object instance, Method method) {
        super(instance, method);
    }

    public HandlerExecutionImpl addResolvers(HandlerMethodArgumentResolver resolver) {
        this.resolver = resolver;
        return this;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) this.invoke(request, request, response, request.getSession());
    }

    private Object invoke(HttpServletRequest request, Object... providedArguments) {
        Object[] args = getMethodArgumentValues(request, providedArguments);
        try {
            return method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            logger.error(e.getTargetException().getMessage(), e.getTargetException());
            throw new RuntimeException(e);
        }
    }

    private Object[] getMethodArgumentValues(HttpServletRequest request, Object... providedArgs) {
        MethodParameter[] parameters = getMethodParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            MethodParameter parameter = parameters[i];

            args[i] = findProvidedArgument(parameter, providedArgs);

            if (args[i] != null) {
                continue;
            }

            if (this.resolver.supportsParameter(parameter)) {
                args[i] = this.resolver.resolveArgument(parameter, request);
            }
        }

        return args;
    }

    private static Object findProvidedArgument(MethodParameter parameter, Object... providedArgs) {
        if (providedArgs == null) {
            return null;
        }

        for (Object providedArg : providedArgs) {
            if (parameter.getParameterType().isInstance(providedArg)) {
                return providedArg;
            }
        }

        return null;
    }
}

package core.mvc.tobe;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * @author KingCjy
 */
public class HandlerMethod {

    protected Method method;
    protected Object instance;

    protected MethodParameter[] parameters;

    protected HandlerMethod(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        parameters = initMethodParameters();
    }

    private MethodParameter[] initMethodParameters() {
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        int count = this.method.getParameterCount();

        MethodParameter[] result = new MethodParameter[count];

        for (int i = 0; i < count; i++) {
            String[] parameterNames = nameDiscoverer.getParameterNames(this.method);
            MethodParameter methodParameter = new MethodParameter(this.method, parameterNames[i], i);
            result[i] = methodParameter;
        }

        return result;
    }

    protected Method getMethod() {
        return this.method;
    }

    protected Object getInstance() {
        return this.instance;
    }

    protected MethodParameter[] getMethodParameters() {
        return parameters;
    }

    public Class<?> getReturnType() {
        return method.getReturnType();
    }
}

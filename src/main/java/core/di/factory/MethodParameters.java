package core.di.factory;

import java.lang.reflect.Method;
import java.util.List;

public class MethodParameters extends ParameterNameDiscoverUtils.Parameters {
    private final Method method;

    public MethodParameters(Method method, List<ParameterNameDiscoverUtils.ParameterTypeName> parameterTypeNames) {
        super(parameterTypeNames);
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "MethodParameters{" +
                "method=" + method +
                ", parameterTypeNames=" + parameterTypeNames +
                '}';
    }
}

package support.parameter;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

public class MethodParameterNameDiscover {

    private static ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    public static String[] getParameterName(Method method) {
        return discoverer.getParameterNames(method);
    }
}

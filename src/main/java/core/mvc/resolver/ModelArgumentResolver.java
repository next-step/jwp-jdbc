package core.mvc.resolver;

import core.mvc.MethodParameter;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

public class ModelArgumentResolver extends AbstractMethodArgumentResolver {

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public boolean supports(MethodParameter parameter) {
        return ModelSupports(parameter);
    }

    private boolean ModelSupports(MethodParameter parameter) {
        return parameter.getAnnotations().length == 0
                && !parameter.getParameterType().isPrimitive()
                && !isPrimitiveWrapperType(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {


        Map<String, String[]> parameterMap = request.getParameterMap();

        Constructor<?> constructor = Arrays.stream(methodParameter.getParameterType().getConstructors())
                .filter(candidateConstructor -> findConstructor(candidateConstructor, parameterMap))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        String[] parameterNames = nameDiscoverer.getParameterNames(constructor);
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        Object[] args = IntStream.range(0, parameterNames.length)
                .boxed()
                .map(i -> getValueWithType(parameterTypes[i], parameterMap.get(parameterNames[i])[0]))
                .toArray();

        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    private boolean findConstructor(Constructor<?> constructor, Map<String, String[]> parameterMap) {

        String[] parameterNames = nameDiscoverer.getParameterNames(constructor);

        if (parameterNames == null) {
            return false;
        }

        return IntStream.range(0, parameterNames.length)
                .allMatch(i -> parameterMap.get(parameterNames[i]) != null);
    }
}

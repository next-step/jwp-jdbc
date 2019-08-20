package core.di.factory;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

public class ParameterNameDiscoverUtils {

    private final static ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public static MethodParameters toMethodParmeters(Method method) {
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Class<?>[] parameterTypes = method.getParameterTypes();
        return new MethodParameters(method, toParameterTypeNames(parameterNames, parameterTypes));
    }

    public static ConstructorParameters toConstructorParameters(Constructor constructor) {
        String[] parameterNames = nameDiscoverer.getParameterNames(constructor);
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        return new ConstructorParameters(constructor, toParameterTypeNames(parameterNames, parameterTypes));
    }

    private static List<ParameterTypeName> toParameterTypeNames(String[] parameterNames, Class<?>[] parameterTypes) {
        return IntStream.range(0, parameterNames.length)
                .mapToObj(i -> new ParameterTypeName(parameterNames[i], parameterTypes[i]))
                .collect(toList());
    }

    public static class ParameterTypeName {
        private final String name;
        private final Class<?> type;

        public ParameterTypeName(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }

        @Override
        public String toString() {
            return "ParameterTypeName{" +
                    "name='" + name + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

    public static class Parameters {
        protected final List<ParameterTypeName> parameterTypeNames;

        private final Set<String> parameterNames;

        public Parameters(List<ParameterTypeName> parameterTypeNames) {
            this.parameterTypeNames = parameterTypeNames;
            this.parameterNames = this.parameterTypeNames
                    .stream()
                    .map(parameterName -> parameterName.getName())
                    .collect(toCollection(() -> new TreeSet<String>(String.CASE_INSENSITIVE_ORDER)));
        }

        public List<ParameterTypeName> getParameterTypeNames() {
            return this.parameterTypeNames;
        }

        public boolean isMatchedParamNames(List<String> names) {
            return names
                    .stream()
                    .collect(toCollection(() -> new TreeSet<String>(String.CASE_INSENSITIVE_ORDER)))
                    .containsAll(this.parameterNames);
        }

        @Override
        public String toString() {
            return "Parameters{" +
                    "parameterTypeNames=" + parameterTypeNames +
                    '}';
        }
    }
}

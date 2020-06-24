package core.containter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SingletonContainer {
    private static final Logger logger = LoggerFactory.getLogger(SingletonContainer.class);

    private static final Map<Class<?>, Object> singletonInstances = new HashMap<>();

    private SingletonContainer() {}

    public static void addSingletons(Collection<Object> instances) {
        if (instances == null) {
            throw new IllegalArgumentException("Instance collection is null");
        }

        singletonInstances.putAll(
                instances.stream()
                        .collect(Collectors.toMap(Object::getClass, Function.identity()))
        );

        logger.debug("Singleton container has been initialized");
        SingletonContainer.singletonInstances.keySet()
                .forEach(clazz -> logger.debug("[{}] has been added to singleton container", clazz));
    }

    public static <T> T getInstance(final Class<T> clazz) {
        return (T) singletonInstances.get(clazz);
    }
}

package core.containter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SingletonContainer {
    private static final Logger logger = LoggerFactory.getLogger(SingletonContainer.class);

    private final Map<Class<?>, Object> instances;

    public SingletonContainer(Collection<Object> instances) {
        if (instances == null) {
            throw new IllegalArgumentException("Instance collection is null");
        }

        this.instances = instances.stream()
                .collect(Collectors.toMap(Object::getClass, Function.identity()));

        logger.debug("Singleton container has been initialized");
        this.instances.keySet()
                .forEach(clazz -> logger.debug("[{}] has been added to singleton container", clazz));
    }

    public <T> T getInstance(final Class<T> clazz) {
        return (T) instances.get(clazz);
    }
}

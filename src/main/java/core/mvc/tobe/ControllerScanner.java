package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author KingCjy
 */
public class ControllerScanner {

    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    private Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(ConfigurationBuilder.build(basePackage).setScanners(
                new SubTypesScanner(false),
                new TypeAnnotationsScanner()
        ));
    }

    public Map<Class<?>, Object> getInstantiateControllers() {
        Map<Class<?>, Object> result = new LinkedHashMap<>();
        Set<Class<?>> controllers = findControllers();

        for (Class<?> controller : controllers) {
            result.put(controller, createInstance(controller));
        }

        return result;
    }

    private Set<Class<?>> findControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Object createInstance(Class<?> targetClass) {
        try {
            Constructor constructor = targetClass.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            logger.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            logger.error(e.getTargetException().getMessage(), e.getTargetException());
        }

        return null;
    }
}

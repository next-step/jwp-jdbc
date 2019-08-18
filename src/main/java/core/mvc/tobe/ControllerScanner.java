package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static core.util.ReflectionUtil.setMethodNameByField;

public class ControllerScanner {
    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> preInitiatedControllers = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(preInitiatedControllers);
    }

    Map<Class<?>, Object> instantiateControllers(Set<Class<?>> preInitiatedControllers) {
        Map<Class<?>, Object> controllers = Maps.newHashMap();
        try {
            for (Class<?> clazz : preInitiatedControllers) {
                Object controller = clazz.newInstance();
                populateController(controller);
                controllers.put(clazz, controller);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
        }

        return controllers;
    }

    private void populateController(final Object controller) {
        final Field[] declaredFields = controller.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            SingletonRegistry.getSingleton(field.getName())
                .ifPresent(value -> injectBySetterMethod(value, controller, field));
        }
    }

    private void injectBySetterMethod(Object value, Object controller, Field field) {
        String setter = setMethodNameByField(field.getName());
        try {
            Method method = controller.getClass().getMethod(setter, field.getType());
            method.invoke(controller, field.getType().cast(value));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.warn("[ {}.{} into {} ] setter injection is failed: {}", controller.getClass(), setter, value.getClass(), e.getMessage());
        }
    }

}

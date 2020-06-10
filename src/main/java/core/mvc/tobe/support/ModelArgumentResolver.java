package core.mvc.tobe.support;

import core.mvc.tobe.MethodParameter;
import core.util.ReflectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static core.util.ReflectionUtils.hasFieldMethod;
import static core.util.StringUtil.upperFirstChar;

public class ModelArgumentResolver implements ArgumentResolver {

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return !isSimpleType(methodParameter.getType());
    }

    private boolean isSimpleType(Class<?> clazz) {
        return ClassUtils.isPrimitiveOrWrapper(clazz)
                || CharSequence.class.isAssignableFrom(clazz);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        try {
            return resolveArgumentInternal(methodParameter, request, response);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(methodParameter.getType() + " Constructor access failed", e);
        } catch (InstantiationException e) {
            throw new IllegalStateException(methodParameter.getType() + " Instantiation failed", e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(methodParameter.getType() + " target invoke failed", e);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(methodParameter.getType() + " method not found", e);
        }
    }

    private Object resolveArgumentInternal(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        Class<?> clazz = methodParameter.getType();
        Object argument = getDefaultInstance(clazz, request);

        for (Field field : clazz.getDeclaredFields()) {
            populateArgument(argument, clazz, field, request);
        }

        return argument;
    }

    private void populateArgument(Object target, Class<?> clazz, Field field, HttpServletRequest request) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        final String setterMethod = "set" + upperFirstChar(field.getName());

        if (hasFieldMethod(clazz, setterMethod, field.getType())) {
            final Method method = clazz.getDeclaredMethod(setterMethod, field.getType());
            method.invoke(target, ReflectionUtils.convertStringValue(request.getParameter(field.getName()), field.getType()));
        }
    }

    private <T> T getDefaultInstance(Class<T> clazz, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Constructor constructor : clazz.getConstructors()) {
            final String[] parameterNames = nameDiscoverer.getParameterNames(constructor);
            assert parameterNames != null;

            final Class[] parameterTypes = constructor.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                args[i] = parameterTypes[i].cast(request.getParameter(parameterNames[i]));
            }

            final Object arg = constructor.newInstance(args);

            return clazz.cast(arg);
        }

        throw new IllegalStateException("[" + clazz.getName() + "] supported constructor is empty");
    }

}

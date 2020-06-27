package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.exception.CoreException;
import core.exception.CoreExceptionStatus;
import core.mvc.HandlerMapping;
import lombok.Getter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@Getter
public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(String... basePackages) {
        Map<HandlerKey, HandlerExecution> handlerExecutionMap = Maps.newHashMap();

        for (String basePackage : basePackages) {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class, true);
            for (Class<?> clazz : classes) {
                putHandlerExecution(clazz, handlerExecutionMap);
            }
        }
        this.handlerExecutions = new HandlerExecutions(handlerExecutionMap);

        handlerExecutionMap.entrySet().forEach(entry -> {
            logger.info("HandlerKey : {}, HandlerExecution : {}", entry.getKey(), entry.getValue());
        });
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(requestUri, rm);
    }

    private void putHandlerExecution(Class<?> clazz, Map<HandlerKey, HandlerExecution> handlerExecutionMap) {
        Object instance = getInstance(clazz);
        for (Method method : clazz.getDeclaredMethods()) {
            boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
            if (isRequestMapping) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
                HandlerExecution handlerExecution = new HandlerExecution(instance, method, requestMapping.value());
                handlerExecutionMap.put(handlerKey, handlerExecution);
            }
        }
    }

    private Object getInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new CoreException(CoreExceptionStatus.CLASS_NEW_INSTANCE_FAIL, e);
        }
    }
}

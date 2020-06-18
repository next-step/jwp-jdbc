package core.mvc.tobe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.DispatcherServlet;
import core.mvc.tobe.support.PathVariableHandlerMethodArgumentResolver;
import core.mvc.tobe.support.RequestBodyHandlerMethodArgumentResolver;
import core.mvc.tobe.support.RequestParamHandlerMethodArgumentResolver;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();
    private HandlerMethodArgumentResolver resolver;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        Map<Class<?>, Object> instantiateControllers = controllerScanner.getInstantiateControllers();
        Set<Method> requestMappingMethods = findRequestMappingMethods(instantiateControllers.keySet());

        initResolvers();
        initHandlerExecutions(instantiateControllers, requestMappingMethods);
    }

    private void initResolvers() {
        ObjectMapper objectMapper = new ObjectMapper();
        HandlerMethodArgumentResolverComposite handlerMethodArgumentResolverComposite = new HandlerMethodArgumentResolverComposite(
                new PathVariableHandlerMethodArgumentResolver(),
                new RequestBodyHandlerMethodArgumentResolver(objectMapper),
                new RequestParamHandlerMethodArgumentResolver(objectMapper)
        );

        this.resolver = handlerMethodArgumentResolverComposite;
    }

    private void initHandlerExecutions(Map<Class<?>, Object> instantiateControllers, Set<Method> requestMappingMethods) {
        for (Method method : requestMappingMethods) {
            Set<HandlerKey> handlerKeys = createHandlerKeys(method);
            HandlerExecution handlerExecution = new HandlerExecutionImpl(instantiateControllers.get(method.getDeclaringClass()), method)
                    .addResolvers(resolver);
            handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        }
    }

    private Set<HandlerKey> createHandlerKeys(Method method) {
        Set<HandlerKey> result = new LinkedHashSet<>();
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        String path = getPath(method);

        RequestMethod[] requestMappings = requestMapping.method();

        if (requestMapping.method().length == 0) {
            requestMappings = RequestMethod.values();
        }

        logger.info("Path : {}, Controller : {}", path, method.getDeclaringClass());

        return Arrays.stream(requestMappings)
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .collect(Collectors.toSet());
    }

    private String getPath(Method method) {
        RequestMapping classMapping = method.getDeclaringClass().getDeclaredAnnotation(RequestMapping.class);
        RequestMapping methodMapping = method.getDeclaredAnnotation(RequestMapping.class);

        String classPath = classMapping == null ? "" : classMapping.value();

        return classPath + methodMapping.value();
    }

    private Set<Method> findRequestMappingMethods(Set<Class<?>> controllers) {
        Set<Method> requestMappingMethods = new LinkedHashSet<>();
        controllers.forEach(targetClass -> requestMappingMethods.addAll(ReflectionUtils.getAllMethods(targetClass, ReflectionUtils.withAnnotation(RequestMapping.class))));

        return requestMappingMethods;
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());

        return handlerExecutions.keySet().stream()
                .filter(key -> key.matches(requestUri, rm))
                .map(key -> handlerExecutions.get(key))
                .findAny()
                .orElse(null);
    }
}

package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import core.mvc.MethodParameters;
import core.mvc.resolver.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestMappingHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(RequestMappingHandlerMapping.class);

    private List<MethodArgumentResolver> argumentResolvers;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();
    private ControllerScanner controllerScanner;

    public RequestMappingHandlerMapping(Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        initialize();
    }

    @Override
    public void initialize() {
        argumentResolvers = Arrays.asList(new PathVariableArgumentResolver(), new RequestParamMethodArgumentResolver(),
                new RequestResponseBodyProcessor(), new ServletRequestMethodArgumentResolver(), new ServletResponseArgumentResolver(),
                new SessionMethodArgumentResolver(), new PrimitiveWrapperArgumentResolver(), new ModelArgumentResolver());

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        controllers.forEach((controller, controllerInstance) -> {

            Method[] methods = getMethodsWithAnnotation(controller, RequestMapping.class);

            Arrays.stream(methods)
                    .forEach(method -> {
                        RequestMapping requestMapping = getAnnotationInMethod(method, RequestMapping.class);
                        RequestMethod[] requestMethods = getRequestMethods(requestMapping);
                        List<HandlerKey> handlerKeys = createHandlerKeys(controller, requestMapping, requestMethods);
                        registerMappings(controllerInstance, method, handlerKeys);
                    });
        });
    }

    private List<HandlerKey> createHandlerKeys(Class<?> controller, RequestMapping requestMapping, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(method -> createHandlerKey(controller, requestMapping.value(), method))
                .collect(Collectors.toList());
    }

    private HandlerKey createHandlerKey(Class<?> controller, String path, RequestMethod method) {
        if (controller.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping annotation = controller.getAnnotation(RequestMapping.class);
            String prefix = annotation.value();
            return new HandlerKey(prefix + path, method);
        }

        return new HandlerKey(path, method);
    }

    private void registerMappings(Object controller, Method method, List<HandlerKey> handlerKeys) {

        MethodParameters methodParameters = new MethodParameters(new LocalVariableTableParameterNameDiscoverer(), method);

        handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(controller, method, methodParameters, argumentResolvers)));
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }

        return requestMethods;
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {

        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());

        Set<HandlerKey> handlerKeys = handlerExecutions.keySet();
        HandlerKey key = handlerKeys.stream()
                .filter(handlerKey -> handlerKey.isUrlMatch(request.getRequestURI(), rm))
                .findFirst()
                .orElse(new HandlerKey(requestUri, rm));

        return handlerExecutions.get(key);
    }

    private <T extends Annotation> T getAnnotationInMethod(Method method, Class<T> annotation) {
        return method.getAnnotation(annotation);
    }

    private <T extends Annotation> Method[] getMethodsWithAnnotation(Class<?> clazz, Class<T> annotation) {
        Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(annotation))
                .toArray(Method[]::new);
    }

}

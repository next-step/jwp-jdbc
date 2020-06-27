package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import core.exception.CoreException;
import core.exception.CoreExceptionStatus;
import core.util.PathPatternUtils;

import java.util.Map;
import java.util.stream.Collectors;

public class HandlerExecutions {
    private Map<HandlerKey, HandlerExecution> handlerExecutions;

    public HandlerExecutions(Map<HandlerKey, HandlerExecution> handlerExecutions) {
        this.handlerExecutions = handlerExecutions;
    }

    public HandlerExecution get(String requestUri, RequestMethod rm) {
        Map<HandlerKey, HandlerExecution> handlerMap = getMatchPathVariablesMap(requestUri);
        handlerMap = getMatchMethodMap(handlerMap, rm);
        if (handlerMap.size() == 0) {
            throw new CoreException(CoreExceptionStatus.NOT_ALLOW_METHOD);
        }

        if (handlerMap.size() > 1) {
            throw new CoreException(CoreExceptionStatus.INVALID_PATH_VARIABLE);
        }
        return handlerMap.values().stream().findFirst().get();
    }

    public Map<HandlerKey, HandlerExecution> getMatchPathVariablesMap(String requestUri) {
        Map<HandlerKey, HandlerExecution> handlerMap = handlerExecutions.entrySet().stream()
                .filter(handlerEntry -> PathPatternUtils.matchPathVariables(handlerEntry.getKey().getUrl(), requestUri))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (handlerMap.size() == 0) {
            throw new CoreException(CoreExceptionStatus.INVALID_PATH_VARIABLE);
        }
        return handlerMap;
    }

    public Map<HandlerKey, HandlerExecution> getMatchMethodMap(Map<HandlerKey, HandlerExecution> handlerMap, RequestMethod requestMethod) {
        return handlerMap.entrySet().stream()
                .filter(handler -> handler.getKey().equalsMethod(requestMethod))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

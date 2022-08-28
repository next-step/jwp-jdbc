package core.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HandlerInterceptorRegistry {

    private final List<HandlerInterceptorExecution> executions = new ArrayList<>();

    public HandlerInterceptorExecution addInterceptor(HandlerInterceptor interceptor, String... pathPatterns) {
        final HandlerInterceptorExecution execution = new HandlerInterceptorExecution(interceptor, pathPatterns);
        executions.add(execution);
        return execution;
    }

    public List<HandlerInterceptor> urlMatchInterceptors(String url) {
        return executions.stream()
            .map(execution -> execution.getHandlerInterceptor(url))
            .flatMap(Optional::stream)
            .collect(Collectors.toUnmodifiableList());
    }
}

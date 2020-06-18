package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    private Set<HandlerMethodArgumentResolver> resolvers = new LinkedHashSet<>();

    public HandlerMethodArgumentResolverComposite(HandlerMethodArgumentResolver... handlerMethodArgumentResolvers) {
        this.resolvers.addAll(Arrays.asList(handlerMethodArgumentResolvers));
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return getArgumentResolver(parameter) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        return getArgumentResolver(parameter).resolveArgument(parameter, request);
    }

    private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
        return this.resolvers.stream()
                .filter(resolver -> resolver.supportsParameter(parameter))
                .findAny()
                .orElse(null);
    }
}

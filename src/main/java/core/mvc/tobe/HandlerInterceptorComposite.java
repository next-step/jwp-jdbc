package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author KingCjy
 */
public class HandlerInterceptorComposite implements HandlerInterceptor {

    private Set<HandlerInterceptor> handlerInterceptors = new LinkedHashSet<>();

    public HandlerInterceptorComposite(HandlerInterceptor ... handlerInterceptors) {
        this.handlerInterceptors.addAll(Arrays.asList(handlerInterceptors));
    }

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response) {
        this.handlerInterceptors.forEach(interceptor -> interceptor.preHandle(request, response));
    }

    @Override
    public void postHandler(HttpServletRequest request, HttpServletResponse response) {
        this.handlerInterceptors.forEach(interceptor -> interceptor.postHandler(request, response));
    }
}

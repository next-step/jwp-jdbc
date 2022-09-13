package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.util.PathAnalyzer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class InterceptorRegistry {

    private final Map<String, Interceptor> targetInterceptors = new HashMap<>();

    public void addInterceptor(String path, Interceptor interceptor) {
        this.targetInterceptors.put(path, interceptor);
    }

    public void preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
        this.targetInterceptors.entrySet().stream()
                .filter(entry -> this.isTargetPath(httpServletRequest, entry.getKey()))
                .forEach(entry -> entry.getValue().preHandle(httpServletRequest, httpServletResponse, handler));
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) {
        this.targetInterceptors.entrySet().stream()
                .filter(entry -> this.isTargetPath(httpServletRequest, entry.getKey()))
                .forEach(entry -> entry.getValue().postHandle(httpServletRequest, httpServletResponse, handler, modelAndView));
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception exception) {
        this.targetInterceptors.entrySet().stream()
                .filter(entry -> this.isTargetPath(httpServletRequest, entry.getKey()))
                .forEach(entry -> entry.getValue().afterCompletion(httpServletRequest, httpServletResponse, handler, exception));
    }

    private boolean isTargetPath(HttpServletRequest httpServletRequest, String targetPathPattern) {
        String requestedUri = httpServletRequest.getRequestURI();
        return PathAnalyzer.getInstance().isTargetPath(targetPathPattern, requestedUri);
    }

}

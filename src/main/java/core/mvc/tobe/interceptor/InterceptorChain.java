package core.mvc.tobe.interceptor;

import com.google.common.collect.Lists;
import core.mvc.ModelAndView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
public class InterceptorChain {
    private List<HandlerInterceptor> matchedInterceptors = Lists.newArrayList();

    public InterceptorChain(List<HandlerInterceptor> matchedInterceptors) {
        if (!CollectionUtils.isEmpty(matchedInterceptors)) {
            this.matchedInterceptors.addAll(matchedInterceptors);
        }
    }

    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (CollectionUtils.isEmpty(matchedInterceptors)) {
            return true;
        }

        for (HandlerInterceptor interceptor : matchedInterceptors) {
            if (!interceptor.preHandle(request, response, handler)) {
                interceptor.afterCompletion(request, response, handler, null);
                return false;
            }
        }

        return true;
    }

    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (CollectionUtils.isEmpty(matchedInterceptors)) {
            return;
        }

        for (int i = matchedInterceptors.size() - 1; i >= 0; i--) {
            HandlerInterceptor handlerInterceptor = matchedInterceptors.get(i);
            handlerInterceptor.postHandle(request, response, handler, modelAndView);
        }
    }

    public void applyAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        if (CollectionUtils.isEmpty(matchedInterceptors)) {
            return;
        }

        for (int i = matchedInterceptors.size() - 1; i >= 0; i--) {
            HandlerInterceptor handlerInterceptor = matchedInterceptors.get(i);
            handlerInterceptor.afterCompletion(request, response, handler, exception);
        }
    }
}
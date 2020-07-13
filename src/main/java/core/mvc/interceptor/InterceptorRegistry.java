package core.mvc.interceptor;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class InterceptorRegistry {

    private List<Interceptor> interceptors = new ArrayList<>();

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public void pre(HttpServletRequest request, HttpServletResponse response, Object object) {
        interceptors.forEach(interceptor -> interceptor.pre(request, response, object));
    }

    public void post(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) {
        interceptors.forEach(interceptor -> interceptor.post(request, response, object, modelAndView));
    }
}

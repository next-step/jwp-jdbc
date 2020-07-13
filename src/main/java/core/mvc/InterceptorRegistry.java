package core.mvc;

import jdk.nashorn.internal.objects.annotations.Getter;
import next.interceptor.Interceptor;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created By kjs4395 on 7/13/20
 */
public class InterceptorRegistry {
    private List<Interceptor> interceptors = new ArrayList<>();

    public void register() {
        Reflections reflections = new Reflections("next");
        Set<Class<? extends Interceptor>> findClass = reflections.getSubTypesOf(Interceptor.class);
        try {
            for (Class clazz : findClass) {
                interceptors.add((Interceptor) clazz.newInstance());
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Illegal Interceptor");
        }
    }

    public void executePreHandle(HttpServletRequest request, HttpServletResponse response) {
        for(Interceptor interceptor : interceptors) {
            interceptor.preHandle(request,response);
        }
    }

    public void executePostHandle(HttpServletRequest request, HttpServletResponse response) {
        for(Interceptor interceptor : interceptors) {
            interceptor.postHandle(request,response);
        }
    }
}

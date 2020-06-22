package core.mvc.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Interceptor {
    void preProcess(HttpServletRequest request, HttpServletResponse response);
    void postProcess(HttpServletRequest request, HttpServletResponse response);
}

package core.mvc.tobe.args;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.Map;

public class HttpRequestArgs implements ArgsResolver {
    private HttpServletRequest request;

    public HttpRequestArgs(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public boolean isArgs(Map.Entry<String, Parameter> parameterEntry) {
        Parameter parameter = parameterEntry.getValue();
        return parameter.getType().equals(HttpServletRequest.class);
    }

    @Override
    public Object getArgs(Map.Entry<String, Parameter> parameterEntry) {
        return this.request;
    }
}

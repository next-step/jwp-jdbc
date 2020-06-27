package core.mvc.tobe.args;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.Map;

public class HttpResponseArgs implements ArgsResolver {
    private HttpServletResponse response;

    public HttpResponseArgs(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public boolean isArgs(Map.Entry<String, Parameter> parameterEntry) {
        Parameter parameter = parameterEntry.getValue();
        return parameter.getType().equals(HttpServletResponse.class);
    }

    @Override
    public Object getArgs(Map.Entry<String, Parameter> parameterEntry) {
        return this.response;
    }
}

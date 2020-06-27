package core.mvc.tobe.args;

import core.mvc.RequestParameters;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;

public class RequestParametersArgs implements ArgsResolver {
    private RequestParameters requestParameters;

    public RequestParametersArgs(RequestParameters requestParameters) {
        this.requestParameters = requestParameters;
    }

    @Override
    public boolean isArgs(Map.Entry<String, Parameter> parameterEntry) {
        String name = parameterEntry.getKey();
        Object requestParameter = this.requestParameters.getParameter(name);
        return Objects.isNull(requestParameter);
    }

    @Override
    public Object getArgs(Map.Entry<String, Parameter> parameterEntry) {
        Parameter parameter = parameterEntry.getValue();
        return this.requestParameters.getBodyObject(parameter.getType());
    }
}

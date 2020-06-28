package core.mvc.tobe.args;

import core.mvc.PathVariables;
import core.mvc.RequestParameters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArgsResolvers {
    private List<ArgsResolver> argsResolvers = new ArrayList<>();

    public ArgsResolvers(PathVariables pathVariables,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         RequestParameters requestParameters) {
        this.argsResolvers.add(new PathVariableArgs(pathVariables));
        this.argsResolvers.add(new HttpRequestArgs(request));
        this.argsResolvers.add(new HttpResponseArgs(response));
        this.argsResolvers.add(new RequestParametersArgs(requestParameters));
    }

    public Optional<ArgsResolver> getArgsResolver(Map.Entry<String, Parameter> parameterEntry) {
        return argsResolvers.stream()
                .filter(argsResolver -> argsResolver.isArgs(parameterEntry))
                .findAny();
    }
}

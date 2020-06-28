package core.mvc;

import core.mvc.tobe.args.ArgsResolver;
import core.mvc.tobe.args.ArgsResolvers;
import lombok.Getter;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Getter
public class MethodParameters {
    Map<String, Parameter> parameters = new LinkedHashMap<>();

    public MethodParameters(Method method) {
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Parameter[] methodParameters = method.getParameters();

        for (int i = 0; i < parameterNames.length; i++) {
            parameters.put(parameterNames[i], methodParameters[i]);
        }
    }

    public Object[] getArgs(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        RequestParameters requestParameters = new RequestParameters(request);
        PathVariables pathVariables = new PathVariables(path, request.getRequestURI());
        ArgsResolvers argsResolvers = new ArgsResolvers(pathVariables, request, response, requestParameters);

        List<Object> args = new ArrayList<>();
        for (Map.Entry<String, Parameter> parameterEntry : parameters.entrySet()) {
            Optional<ArgsResolver> optionalArgsResolver = argsResolvers.getArgsResolver(parameterEntry);
            if (optionalArgsResolver.isPresent()) {
                ArgsResolver resolver = optionalArgsResolver.get();
                args.add(resolver.getArgs(parameterEntry));
                continue;
            }

            String name = parameterEntry.getKey();
            args.add(requestParameters.getParameter(name));
        }

        return args.toArray();
    }
}

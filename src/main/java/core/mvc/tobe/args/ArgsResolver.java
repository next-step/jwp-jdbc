package core.mvc.tobe.args;

import java.lang.reflect.Parameter;
import java.util.Map;

public interface ArgsResolver {
    boolean isArgs(Map.Entry<String, Parameter> parameterEntry);

    Object getArgs(Map.Entry<String, Parameter> parameterEntry);
}

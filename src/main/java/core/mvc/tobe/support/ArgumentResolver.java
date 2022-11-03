package core.mvc.tobe.support;

import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ArgumentResolver {

    boolean supports(MethodParameter methodParameter);

    Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) throws IOException;

}

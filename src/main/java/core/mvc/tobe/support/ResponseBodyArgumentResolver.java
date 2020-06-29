package core.mvc.tobe.support;

import core.annotation.web.ResponseBody;
import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class ResponseBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(ResponseBodyArgumentResolver.class);

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, ResponseBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        try {
            String values = request.getReader().lines()
                    .collect(Collectors.joining());
            return JsonUtils.toObject(values, methodParameter.getType());
        } catch (IOException e) {
            logger.error("Exception : ", e);
            throw new IllegalStateException(methodParameter.getType() + " json parsing failed", e);
        }
    }

}

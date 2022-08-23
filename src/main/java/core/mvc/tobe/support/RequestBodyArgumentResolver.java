package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

    private Logger logger = LoggerFactory.getLogger(RequestParamArgumentResolver.class);

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        RequestBody annotation = getAnnotation(methodParameter, RequestBody.class);
        boolean required = annotation.required();

        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = request.getReader();
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                sb.append(charBuffer, 0, bytesRead);
            }

            String requestBodyString = sb.toString();

            if (required && requestBodyString.isEmpty()) {
                throw new IllegalArgumentException("RequestBody 필수");
            }

            return JsonUtils.toObject(sb.toString(), methodParameter.getType());

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }
}

package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import core.util.Args;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

/**
 * Created by iltaek on 2020/07/02 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

    private static final String ILLEGAL_CONTENT_TYPE = "지원하지 않는 Content Type 입니다.: ";
    private static final String CANNOT_READ_BODY = "Body를 읽을 수 없습니다.";

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        Args.check(isJsonBody(request), ILLEGAL_CONTENT_TYPE + request.getContentType());
        String result = readRequestBody(request);
        return JsonUtils.toObject(result, methodParameter.getType());
    }

    private boolean isJsonBody(HttpServletRequest request) {
        return request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE);
    }

    private String readRequestBody(HttpServletRequest request) {
        try {
            return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new IllegalArgumentException(CANNOT_READ_BODY, e);
        }
    }
}

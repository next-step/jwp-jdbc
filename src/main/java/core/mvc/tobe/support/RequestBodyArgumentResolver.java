package core.mvc.tobe.support;

import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyJson = parseBodyJson(request);

        return JsonUtils.toObject(bodyJson, methodParameter.getType());
    }

    private String parseBodyJson(HttpServletRequest request) throws IOException {
        String bodyJson = "";
        ServletInputStream inputStream;


            inputStream = request.getInputStream();
            bodyJson = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);


        return bodyJson;
    }
}

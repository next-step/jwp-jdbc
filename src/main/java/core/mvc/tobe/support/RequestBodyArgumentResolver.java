package core.mvc.tobe.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        String bodyJson = parseBodyJson(request);

        return JsonUtils.toObject(bodyJson, methodParameter.getType());
    }

    private String parseBodyJson(HttpServletRequest request) {
        String bodyJson = "";

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = null;

        String line = "";
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                br = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        bodyJson = stringBuilder.toString();
        return bodyJson;
    }
}

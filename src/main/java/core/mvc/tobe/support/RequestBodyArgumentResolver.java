package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.tobe.MethodParameter;
import core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver {

	@Override
	public boolean supports(MethodParameter methodParameter) {
		return supportAnnotation(methodParameter, RequestBody.class);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
		try {
			String jsonBody = IOUtils.toString(request.getReader());
			log.debug("RequestBodyArgumentResolver - jsonBody: {}", jsonBody);
			return StringUtils.fromJson(jsonBody, methodParameter.getType());
		}
		catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
}

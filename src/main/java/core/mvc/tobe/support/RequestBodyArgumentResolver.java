package core.mvc.tobe.support;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.annotation.web.RequestBody;
import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import core.util.IOUtils;

public class RequestBodyArgumentResolver extends AbstractAnnotationArgumentResolver{

	private static final Logger logger = LoggerFactory.getLogger(RequestBodyArgumentResolver.class);

	@Override
	public boolean supports(MethodParameter methodParameter) {
		logger.debug("=========== Request Body Argument Resolver Supports");
		return supportAnnotation(methodParameter, RequestBody.class);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("===========  Request Body Argument Resolver Resolve");
		String contentType = request.getContentType();

		if (contentType == null) {
			throw new IllegalArgumentException("Cannot extract Content-Type");
		}

		try {
			String stringBody = IOUtils.readData(request.getReader(), request.getContentLength());
			return JsonUtils.toObject(stringBody, methodParameter.getType());
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Cannot read Request Body");
		}
	}
}

package core.mvc.tobe.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.mvc.tobe.MethodParameter;
import next.dto.UserCreatedDto;

public class RequestBodyArgumentResolverTest {
	private RequestBodyArgumentResolver argumentResolver = new RequestBodyArgumentResolver();
	private ObjectMapper objectMapper = new ObjectMapper();

	@DisplayName("Request Body Argument Resolver Test")
	@Test
	void RequestParamResolveArguments() throws NoSuchMethodException, JsonProcessingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		UserCreatedDto expect = new UserCreatedDto("pobi", "password", "포비", "pobi@nextstep.camp");

		request.setMethod(HttpMethod.POST.toString());
		request.setRequestURI("/requestBody");
		request.setContentType(MediaType.APPLICATION_JSON.toString());
		request.setContent(objectMapper.writeValueAsString(expect).getBytes(StandardCharsets.UTF_8));

		Method mockStringRequestBodyMethod = MockArgumentResolverController.class.getDeclaredMethod("mockRequestBodyMethod", UserCreatedDto.class);
		MethodParameter mp = new MethodParameter(mockStringRequestBodyMethod, UserCreatedDto.class, new Annotation[]{}, "userCreatedDto");

		UserCreatedDto actual = (UserCreatedDto) argumentResolver.resolveArgument(mp, request, response);

		assertThat(expect).isEqualTo(actual);
	}
}

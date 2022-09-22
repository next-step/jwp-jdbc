package core.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import next.Interceptor.TestInterceptor;
import next.Interceptor.TestInterceptor2;
import next.interceptor.ExecutionTimerInterceptor;

public class HandlerInterceptorRegistryTest {
	@Test
	@DisplayName("여러 인터셉터 실행 테스트")
	public void interceptorsExecutionTest() throws InterruptedException {
		HandlerInterceptorRegistry registry = new HandlerInterceptorRegistry();
		registry.addInterceptor(new ExecutionTimerInterceptor());
		registry.addInterceptor(new TestInterceptor());
		registry.addInterceptor(new TestInterceptor2());

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		registry.preHandle(request, response);
		Thread.sleep(5000);
		registry.postHandle(request, response);
	}
}

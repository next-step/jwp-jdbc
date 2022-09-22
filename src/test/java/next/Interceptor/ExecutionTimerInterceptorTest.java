package next.Interceptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import next.interceptor.ExecutionTimerInterceptor;
import next.interceptor.Interceptor;

public class ExecutionTimerInterceptorTest {

	@Test
	@DisplayName("실행 속도 측정 로그 테스트")
	public void executionTime() throws InterruptedException {
		Interceptor interceptor = new ExecutionTimerInterceptor();

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		interceptor.preHandle(request, response);
		Thread.sleep(5000);
		interceptor.postHandle(request, response);
	}
}

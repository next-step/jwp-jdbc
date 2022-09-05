package next.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.interceptor.Interceptor;

public class TestInterceptor2 implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger(TestInterceptor2.class);

	@Override
	public void preHandle(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Test2 Pre-handle");
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Test2 Post-handle");
	}
}

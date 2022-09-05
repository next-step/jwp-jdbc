package next.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.interceptor.Interceptor;

public class TestInterceptor implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger(TestInterceptor.class);

	@Override
	public void preHandle(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Test Pre-handle");
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Test Post-handle");
	}
}

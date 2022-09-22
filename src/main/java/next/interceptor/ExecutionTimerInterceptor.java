package next.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutionTimerInterceptor implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger(ExecutionTimerInterceptor.class);
	private static final String TIMER_START_HEADER = "PreHandlerStartTime";

	@Override
	public void preHandle(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(TIMER_START_HEADER, System.currentTimeMillis());
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response) {
		long postHandleEndTime = System.currentTimeMillis();
		long preHandleStartTime = (long) request.getAttribute(TIMER_START_HEADER);
		logger.debug("Execution Time : {} ms", postHandleEndTime - preHandleStartTime);
	}
}

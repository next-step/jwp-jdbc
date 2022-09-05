package next.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutionTimerInterceptor implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger(ExecutionTimerInterceptor.class);
	private static final String TIMER_START_HEADER = "PreHandlerStartTime";

	@Override
	public void preHandle(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.setAttribute(TIMER_START_HEADER, System.currentTimeMillis());
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response) {
		long postHandleEndTime = System.currentTimeMillis();
		long preHandleStartTime = (long) request.getSession().getAttribute(TIMER_START_HEADER);

		logger.debug("Execution Time : {} ms", postHandleEndTime - preHandleStartTime);
	}
}

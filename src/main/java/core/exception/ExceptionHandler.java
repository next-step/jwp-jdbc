package core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExceptionHandler {

	public boolean supports(Class<?> clazz);
	
	public void handle(Throwable exception, HttpServletRequest request, HttpServletResponse response) ;
}

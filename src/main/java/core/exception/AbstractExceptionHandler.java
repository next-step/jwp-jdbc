package core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractExceptionHandler<T extends Throwable> implements ExceptionHandler{

	
	Class<T> parameterizedType;
	
	public AbstractExceptionHandler(Class<T> parameterizedType) {
		this.parameterizedType = parameterizedType;
	}
	
	public boolean supports(Class<?> clazz) {
		return this.parameterizedType.isAssignableFrom(clazz);
	}
	
	public void handle(Throwable exception, HttpServletRequest request, HttpServletResponse response) {
		handleInner(this.parameterizedType.cast(exception), request, response);
	}
	
	public abstract void handleInner(T exception, HttpServletRequest request, HttpServletResponse response);
	
}

package core.exception;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExceptionHandlers implements ExceptionHandler {

	private final Set<ExceptionHandler> exceptionHandlers = new HashSet<>();
	private final Map<Class<?>, ExceptionHandler> cache = new HashMap<>();
	
	@Override
	public boolean supports(Class<?> clazz) {
		return getFromCache(clazz).isPresent();
	}

	@Override
	public void handle(Throwable exception, HttpServletRequest request, HttpServletResponse response) {
		ExceptionHandler handler = getFromCache(exception.getClass()).get();
		handler.handle(exception, request, response);
	}
	
	
	private Optional<ExceptionHandler> getFromCache(Class<?> clazz){
		ExceptionHandler handler = this.cache.computeIfAbsent(clazz, (key) -> getFromHandlers(clazz).orElse(null));
		return Optional.ofNullable(handler);
	}
	
	private Optional<ExceptionHandler> getFromHandlers(Class<?> clazz){
		return this.exceptionHandlers.stream()
		.filter(handler-> handler.supports(clazz))
		.findFirst();
	}
	
	
	public void addHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandlers.add(exceptionHandler);
	}
	
	

}

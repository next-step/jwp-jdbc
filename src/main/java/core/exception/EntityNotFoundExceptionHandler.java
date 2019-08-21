package core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

public class EntityNotFoundExceptionHandler extends AbstractExceptionHandler<EntityNotFoundException>{

	public EntityNotFoundExceptionHandler() {
		super(EntityNotFoundException.class);
	}

	@Override
	public void handleInner(EntityNotFoundException exception, HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpStatus.NOT_FOUND.value());
	}
	
	
}

package core.exception;

public class EntityNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 140039963701853737L;

	public EntityNotFoundException() {
		super();
	}

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotFoundException(Throwable cause) {
		super(cause);
	}

	protected EntityNotFoundException(String message, Throwable cause,
			boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

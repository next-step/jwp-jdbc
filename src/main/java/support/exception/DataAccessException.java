package support.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataAccessException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(DataAccessException.class);

    public DataAccessException(Exception e) {
        super(e);
    }

    public static void handleException(Exception e) {
        logger.error("exception occur : {}", e.toString());
        throw new DataAccessException(e);
    }
}

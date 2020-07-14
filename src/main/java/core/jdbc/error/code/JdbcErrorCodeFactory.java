package core.jdbc.error.code;

public class JdbcErrorCodeFactory {

    public static ErrorCode create(String driver) {
        switch (driver) {
            case "org.h2.Driver":
                return new H2ErrorCode();
        }
        return new ErrorCode();
    }
}

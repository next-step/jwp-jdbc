package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParameterSetter {

    private final Object[] args;

    public ParameterSetter(Object[] args) {
        this.args = args;
    }

    public void setArgs(PreparedStatement ps) throws SQLException {
    	JdbcTypeHandlers jdbcTypeHandlers = JdbcTypeHandlers.getInstance();
    	
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            JdbcTypeHandler<?> jdbcTypeHandler = jdbcTypeHandlers.getTypeHandler(arg);
            jdbcTypeHandler.setParameter(ps, i + 1, arg);
        }
    }
}
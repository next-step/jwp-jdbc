package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArgumentPreparedStatementSetter implements PreparedStatementSetter {

    private final Object[] arguments;

    public ArgumentPreparedStatementSetter(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        for (int index = 0; index < arguments.length; index++) {
            ps.setObject(index + 1, arguments[index]);
        }
    }
}

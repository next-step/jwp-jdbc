package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementCreatorImpl implements PreparedStatementCreator {

    private final String sql;
    private final Object[] arguments;

    public PreparedStatementCreatorImpl(String sql, Object[] arguments) {
        this.sql = sql;
        this.arguments = arguments;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement preparedStatement = con.prepareStatement(sql);

        for (int i = 0; i < arguments.length; i++) {
            preparedStatement.setObject(i + 1, arguments[i]);
        }

        return preparedStatement;
    }
}

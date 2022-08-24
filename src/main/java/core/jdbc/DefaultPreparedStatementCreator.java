package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DefaultPreparedStatementCreator implements PreparedStatementCreator {

    private final String sql;
    private final PreparedStatementSetter preparedStatementSetter;

    public DefaultPreparedStatementCreator(final String sql, final Object... arguments) {
        this(sql, new DefaultPreparedStatementSetter(arguments));
    }

    public DefaultPreparedStatementCreator(final String sql, final PreparedStatementSetter preparedStatementSetter) {
        this.sql = sql;
        this.preparedStatementSetter = preparedStatementSetter;
    }

    @Override
    public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
        final PreparedStatement preparedStatement = getPreparedStatement(connection);
        preparedStatementSetter.setValues(preparedStatement);
        return preparedStatement;
    }

    private PreparedStatement getPreparedStatement(final Connection connection) throws SQLException {
        return connection.prepareStatement(this.sql);
    }

}

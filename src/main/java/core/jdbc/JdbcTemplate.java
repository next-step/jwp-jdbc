package core.jdbc;

import core.jdbc.exception.JdbcTemplateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public int execute(final String sql, final Object... arguments) {
        try (
            final Connection con = ConnectionManager.getConnection();
            final Transaction transaction = new Transaction(con);
            final PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            setArguments(pstmt, arguments);

            final int result = pstmt.executeUpdate();
            transaction.commit();

            return result;
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    public <T> Optional<T> queryForObject(final String sql, final RowMapperFunction<T> function, final Object... arguments) {
        return query(sql, () -> {
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(function.apply(resultSet));
            }

            return Optional.empty();
        }, arguments);
    }

    public <T> List<T> queryForList(final String sql, final RowMapperFunction<T> function, final Object... arguments) {
        return query(sql, () -> {
            resultSet = preparedStatement.executeQuery();
            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(function.apply(resultSet));
            }

            return results;
        }, arguments);
    }

    private <T> T query(final String sql, final QuerySupplier<T> supplier, final Object... arguments) {
        initPreparedStatement(sql, arguments);

        try {
            connection.setReadOnly(true);

            return supplier.get();
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        } finally {
            DataSourceUtils.release(connection, preparedStatement, resultSet);
        }
    }

    private void initPreparedStatement(final String sql, final Object[] arguments) {
        try {
            connection = ConnectionManager.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            setArguments(preparedStatement, arguments);
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    private void setArguments(final PreparedStatement preparedStatement, final Object[] arguments) throws SQLException {
        for (int i = 0; i < arguments.length; i++) {
            preparedStatement.setObject((i + 1), arguments[i]);
        }
    }
}

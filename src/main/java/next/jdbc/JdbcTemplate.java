package next.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {

    private final DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void execute(final String sql,
                        final StatementSupplier supplier) {
        final Handler<Void> handler = preparedStatement -> {
            supplier.supply(preparedStatement);
            preparedStatement.executeUpdate();

            return null;
        };

        call(sql, handler);
    }

    public <T> T query(final String sql,
                       final ResultMapper<T> mapper) {
        final Handler<T> handler = preparedStatement -> {
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapper.mapping(resultSet);
            }
        };

        return call(sql, handler);
    }

    public <T> T query(final String sql,
                       final StatementSupplier supplier,
                       final ResultMapper<T> mapper) {
        final Handler<T> handler = preparedStatement -> {
            supplier.supply(preparedStatement);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapper.mapping(resultSet);
            }
        };

        return call(sql, handler);
    }

     private <R> R call(final String sql,
                        final Handler<R> handler) {
         try (final Connection connection = dataSource.getConnection();
              final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             return handler.handle(preparedStatement);
         } catch (final SQLException e) {
             throw new JdbcTemplateException(e);
         }
    }

    @FunctionalInterface
    interface Handler<R> {

        R handle(final PreparedStatement preparedStatement) throws SQLException;
    }
}

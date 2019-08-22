package next.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {

    private static final StatementSupplier DEFAULT_SUPPLIER = ignore -> { };

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

    public <T> List<T> queryList(final String sql,
                                 final ResultMapper<T> mapper) {
        return queryList(sql, DEFAULT_SUPPLIER, mapper);
    }

    public <T> List<T> queryList(final String sql,
                                 final StatementSupplier supplier,
                                 final ResultMapper<T> mapper) {
        final Handler<List<T>> handler = preparedStatement -> {
            supplier.supply(preparedStatement);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                final List<T> results = new ArrayList<>();
                while (resultSet.next()) {
                    final T result = mapper.mapping(resultSet);
                    results.add(result);
                }

                return results;
            }
        };

        return call(sql, handler);
    }

    public <T> Optional<T> querySingle(final String sql,
                                       final ResultMapper<T> mapper) {
        return querySingle(sql, DEFAULT_SUPPLIER, mapper);
    }

    public <T> Optional<T> querySingle(final String sql,
                                       final StatementSupplier supplier,
                                       final ResultMapper<T> mapper) {
        final Handler<Optional<T>> handler = preparedStatement -> {
            supplier.supply(preparedStatement);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                final T result = mapper.mapping(resultSet);
                return Optional.ofNullable(result);
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

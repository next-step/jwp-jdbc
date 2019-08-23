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

    private static final int INDEX_OF_START = 1;

    private final DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void execute(final String sql,
                        final Object... parameters) {
        call(sql, PreparedStatement::executeUpdate, parameters);
    }

    public <T> Optional<T> querySingle(final String sql,
                                       final ResultMapper<T> mapper,
                                       final Object... parameters) {
        return queryList(sql, mapper, parameters)
                .stream()
                .findFirst();
    }

    public <T> List<T> queryList(final String sql,
                                 final ResultMapper<T> mapper,
                                 final Object... parameters) {
        final Handler<List<T>> handler = preparedStatement -> {
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                final List<T> results = new ArrayList<>();
                while (resultSet.next()) {
                    final T result = mapper.mapping(resultSet);
                    results.add(result);
                }

                return results;
            }
        };

        return call(sql, handler, parameters);
    }

    private <R> R call(final String sql,
                       final Handler<R> handler,
                       final Object... parameters) {
         try (final Connection connection = dataSource.getConnection();
              final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             setValues(preparedStatement, parameters);

             return handler.handle(preparedStatement);
         } catch (final SQLException e) {
             throw new JdbcTemplateException(e);
         }
    }

    private void setValues(final PreparedStatement preparedStatement,
                           final Object... parameters) throws SQLException {
        for (int index = INDEX_OF_START; index <= parameters.length; index++) {
            preparedStatement.setObject(index, parameters[index - INDEX_OF_START]);
        }
    }

    @FunctionalInterface
    interface Handler<R> {

        R handle(final PreparedStatement preparedStatement) throws SQLException;
    }
}

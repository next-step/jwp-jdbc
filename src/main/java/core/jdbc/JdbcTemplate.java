package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static core.jdbc.ConnectionManager.getConnection;

public class JdbcTemplate {

    public void execute(final String sql, final Object... values) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = prepareStatement(connection, sql, values);

            preparedStatement.execute();
        } catch (Exception e) {
            throw new JdbcApiException("Fail to execute query : " + e.getMessage());
        }
    }

    public <T> List<T> findAll(final String sql, final RowConverter<T> converter, final Object... values) {
        return executeQuery(sql, converter, values);
    }

    public <T> T findOne(final String sql, final RowConverter<T> converter, final Object... values) {
        List<T> results = findAll(sql, converter, values);
        validateUnique(results);

        return results.isEmpty() ? null : results.get(0);
    }

    private <T> List<T> executeQuery(final String sql, final RowConverter<T> converter, final Object... values) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = prepareStatement(connection, sql, values);

            ResultSet resultSet = executeQuery(preparedStatement);

            return convertToClasses(converter, resultSet);
        } catch (Exception e) {
            throw new JdbcApiException("Fail to execute select query : " + e.getMessage());
        }
    }

    private <T> List<T> convertToClasses(final RowConverter<T> converter, final ResultSet resultSet) throws SQLException {
        List<T> classes = new ArrayList<>();

        while (resultSet.next()) {
            classes.add(convert(converter, resultSet));
        }

        return classes;
    }

    private <T> T convert(final RowConverter<T> converter, final ResultSet resultSet) {
        try {
            return converter.convert(resultSet);
        } catch (Exception e) {
            throw new JdbcApiException("Fail to convert result set to target dao");
        }
    }

    private void validateUnique(final List<?> classes) {
        if (classes.size() > 1) {
            throw new IllegalArgumentException("Query result not unique : " + classes.size());
        }
    }

    private ResultSet executeQuery(final PreparedStatement preparedStatement) {
        try {
            return preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throw new JdbcApiException("Fail to execute query : " + throwables.getMessage());
        }
    }

    private void setValues(final PreparedStatement preparedStatement, final Object... values) throws SQLException {
        for (int idx = 0 ; idx < values.length ; ++idx) {
            preparedStatement.setObject(idx + 1, values[idx]);
        }
    }

    private PreparedStatement prepareStatement(final Connection connection, final String sql, final Object... values) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            setValues(preparedStatement, values);

            return preparedStatement;
        } catch (SQLException throwables) {
            throw new JdbcApiException("Fail to connect to db server : " + throwables.getMessage());
        }
    }
}

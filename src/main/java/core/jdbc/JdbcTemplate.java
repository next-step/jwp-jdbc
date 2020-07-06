package core.jdbc;

import core.jdbc.exception.SqlExecuteException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, PreparedStatementSetter preparedStatementSetter) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatementSetter.setParameter(preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SqlExecuteException(e);
        }
    }

    public <T> T queryForObject(String sql, PreparedStatementSetter preparedStatementSetter, ResultSetReader<T> resultSetReader) {
        List<T> objects = executeSelect(sql, preparedStatementSetter, resultSetReader);
        return objects.get(0);
    }

    public <T> List<T> query(String sql, PreparedStatementSetter preparedStatementSetter, ResultSetReader<T> resultSetReader) {
        return executeSelect(sql, preparedStatementSetter, resultSetReader);
    }

    private <T> List<T> executeSelect(String sql, PreparedStatementSetter preparedStatementSetter, ResultSetReader<T> resultSetReader) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatementSetter.setParameter(preparedStatement);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<T> objects = new ArrayList<>();

                while (resultSet.next()) {
                    T object = resultSetReader.read(resultSet);
                    objects.add(object);
                }

                return objects;
            }
        } catch (SQLException e) {
            throw new SqlExecuteException(e);
        }
    }

}

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

    public void update(String sql, Object[] parameters) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SqlExecuteException(e);
        }
    }

    public <T> T queryForObject(String sql, Object[] parameters, RowMapper<T> rowMapper) {
        List<T> objects = executeSelect(sql, parameters, rowMapper);
        if (objects.isEmpty()) {
            return null;
        }

        return objects.get(0);
    }

    public <T> List<T> query(String sql, Object[] parameters, RowMapper<T> rowMapper) {
        return executeSelect(sql, parameters, rowMapper);
    }

    private <T> List<T> executeSelect(String sql, Object[] parameters, RowMapper<T> rowMapper) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<T> objects = new ArrayList<>();

                while (resultSet.next()) {
                    T object = rowMapper.mapRow(resultSet);
                    objects.add(object);
                }

                return objects;
            }
        } catch (SQLException e) {
            throw new SqlExecuteException(e);
        }
    }

}

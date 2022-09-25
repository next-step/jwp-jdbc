package core.jdbc;

import org.springframework.dao.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultJdbcTemplate implements JdbcTemplate{
    @Override
    public void update(String sql, PreparedStatementSetter preparedStatementSetter) throws DataAccessException {

        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatementSetter.setValues(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(String sql, Object... values) throws DataAccessException {
        PreparedStatementSetter preparedStatementSetter = createPreparedStatementSetter(values);
        update(sql, preparedStatementSetter);
    }

    @Override
    public PreparedStatementSetter createPreparedStatementSetter(Object... values) {
        return new DefaultPreparedStatementSetter(List.of(values));
    }

    @Override
    public <T> T get(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws DataAccessException {
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatementSetter.setValues(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return rowMapper.mapping(resultSet);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T get(String sql, RowMapper<T> rowMapper, Object... values) throws DataAccessException {
        return get(sql, rowMapper, createPreparedStatementSetter(values));
    }

    @Override
    public <T> List<T> findAll(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws DataAccessException {

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatementSetter.setValues(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<T> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(rowMapper.mapping(resultSet));
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> findAll(String sql,
                               RowMapper<T> rowMapper,
                               Object... values) throws DataAccessException {
        return findAll(sql, rowMapper, createPreparedStatementSetter(values));
    }
}

package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JdbcTemplate {

    public int update(PreparedStatementCreator creator, PreparedStatementSetter setter, KeyHolder keyHolder) {
        try (Connection con = ConnectionManager.getConnection()) {
            try (PreparedStatement statement = creator.createPreparedStatement(con)) {
                setter.setStatement(statement);
                int count = statement.executeUpdate();
                setGeneratedKey(statement, keyHolder);
                return count;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private void setGeneratedKey(PreparedStatement statement, KeyHolder keyHolder) throws SQLException {
        if (Objects.isNull(keyHolder)) {
            return;
        }
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                keyHolder.setId(generatedKeys.getLong(1));
            }
        }
    }

    public int update(PreparedStatementCreator creator, PreparedStatementSetter setter) {
        return update(creator, setter, null);
    }

    public int update(String sql, PreparedStatementSetter setter) {
        return update(con -> con.prepareStatement(sql), setter);
    }

    public int update(String sql, PreparedStatementSetter setter, KeyHolder keyHolder) {
        return update(con -> con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS), setter, keyHolder);
    }

    public int update(String sql, Object... parameters) {
        return update(sql, statement -> PreparedStatementSetter.setParameters(statement, parameters));
    }

    public int update(String sql, KeyHolder keyHolder, Object... parameters) {
        return update(sql, statement -> PreparedStatementSetter.setParameters(statement, parameters), keyHolder);
    }

    public <T> List<T> queryForList(PreparedStatementCreator creator, PreparedStatementSetter setter, RowMapper<T> rowMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement statement = creator.createPreparedStatement(con)) {

            setter.setStatement(statement);
            try (ResultSet rs = statement.executeQuery()) {
                List<T> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(rowMapper.mapRow(rs));
                }
                return results;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> queryForList(String sql, PreparedStatementSetter setter, RowMapper<T> rowMapper) {
        return queryForList(con -> con.prepareStatement(sql), setter, rowMapper);
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return queryForList(sql, statement -> PreparedStatementSetter.setParameters(statement, parameters), rowMapper);
    }

    public <T> Optional<T> queryForOptionalObject(PreparedStatementCreator creator, PreparedStatementSetter setter, RowMapper<T> rowMapper) {
        List<T> results = queryForList(creator, setter, rowMapper);
        if (results.size() > 1) {
            throw new DataAccessException("결과 값이 하나 이상입니다.");
        }
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }

    public <T> Optional<T> queryForOptionalObject(String sql, PreparedStatementSetter setter, RowMapper<T> rowMapper) {
        return queryForOptionalObject(con -> con.prepareStatement(sql), setter, rowMapper);
    }

    public <T> Optional<T> queryForOptionalObject(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return queryForOptionalObject(sql, statement -> PreparedStatementSetter.setParameters(statement, parameters), rowMapper);
    }

}
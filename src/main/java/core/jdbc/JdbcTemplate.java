package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {

    public int update(PreparedStatementCreator creator, PreparedStatementSetter setter) {
        try {
            try (Connection con = ConnectionManager.getConnection()) {
                try (PreparedStatement statement = creator.createPreparedStatement(con)) {
                    setter.setStatement(statement);
                    return statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public int update(String sql, PreparedStatementSetter setter) {
        return update(con -> con.prepareStatement(sql), setter);
    }

    public int update(String sql, Object... parameters) {
        return update(sql, statement -> PreparedStatementSetter.setParameters(statement, parameters));
    }

    public <T> List<T> queryForList(PreparedStatementCreator creator, PreparedStatementSetter setter, RowMapper<T> rowMapper) {
        try (Connection con = ConnectionManager.getConnection()) {
            try (PreparedStatement statement = creator.createPreparedStatement(con)) {
                setter.setStatement(statement);

                try (ResultSet rs = statement.executeQuery()) {
                    List<T> results = new ArrayList<>();
                    if (rs.next()) {
                        results.add(rowMapper.mapRow(rs));
                    }
                    return results;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> queryForList(String sql, PreparedStatementSetter setter, RowMapper<T> rowMapper) {
        return queryForList(con -> con.prepareStatement(sql), setter, rowMapper);
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return queryForList(sql, statement -> PreparedStatementSetter.setParameters(statement, parameters),rowMapper);
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

    public <T> T queryForObject(PreparedStatementCreator creator, PreparedStatementSetter setter, RowMapper<T> rowMapper) {
        return queryForOptionalObject(creator, setter, rowMapper).orElse(null);
    }

    public <T> T queryForObject(String sql, PreparedStatementSetter setter, RowMapper<T> rowMapper) {
        return queryForOptionalObject(sql, setter, rowMapper).orElse(null);
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return queryForOptionalObject(sql, statement -> PreparedStatementSetter.setParameters(statement, parameters), rowMapper)
                .orElse(null);
    }

}
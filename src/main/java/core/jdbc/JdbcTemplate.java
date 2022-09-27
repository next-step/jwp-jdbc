package core.jdbc;

import core.jdbc.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate<T> {

    private JdbcTemplate() {}

    private static class JdbcTemplateHolder {
        private static final JdbcTemplate instance = new JdbcTemplate();
    }

    public static JdbcTemplate getInstance() {
        return JdbcTemplateHolder.instance;
    }

    public void update(String sql, PreparedStatementSetter setter) {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.setValues(pstmt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("fail to update");
        }
    }

    public void update(String sql, Object... values) {
        update(sql, createPreparedStatementSetter(values));
    }

    public List<T> query(String sql, RowMapper<T> mapper, PreparedStatementSetter setter) {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.setValues(pstmt);
            return extractResult(pstmt, mapper);
        } catch (SQLException e) {
            throw new DataAccessException("fail to query");
        }
    }

    public List<T> query(String sql, RowMapper<T> mapper, Object... values) {
        return query(sql, mapper, createPreparedStatementSetter(values));
    }

    public T queryForObject(String sql, RowMapper<T> mapper, PreparedStatementSetter setter) {
        final List<T> list = query(sql, mapper, setter);
        if (list.isEmpty()) {
            throw new DataAccessException("fail to query for object");
        }
        return list.get(0);
    }

    public T queryForObject(String sql, RowMapper<T> mapper, Object... values) {
        return queryForObject(sql, mapper, createPreparedStatementSetter(values));
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object... values) {
        return psmt -> {
            for (int i = 0; i < values.length; i++) {
                psmt.setObject(i + 1, values[i]);
            }
        };
    }

    private List<T> extractResult(PreparedStatement pstmt, RowMapper<T> mapper) throws SQLException {
        try(ResultSet rs = pstmt.executeQuery()) {
            List<T> list = new ArrayList<>();
            while(rs.next()) {
                list.add(mapper.mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("fail to convert resultSet");
        }
    }
}

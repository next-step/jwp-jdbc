package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate<T> {
    public void update(String sql, PreparedStatementSetter pstmtSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("update 실행을 실패하였습니다.", e);
        }
    }

    public void update(String sql, Object... values) {
        update(sql, createPreparedStatementSetter(values));
    }

    public List<T> query(String sql, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            return convertToList(rowMapper, pstmt);
        } catch (SQLException e) {
            throw new DataAccessException("query 실행을 실패하였습니다.", e);
        }
    }

    public List<T> query(String sql, RowMapper<T> rowMapper, Object... values) {
        return query(sql, createPreparedStatementSetter(values), rowMapper);
    }

    public T queryForObject(String sql, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            return convertToObject(rowMapper, pstmt);
        } catch (SQLException e) {
            throw new DataAccessException("queryForObject 실행을 실패하였습니다.", e);
        }
    }

    public T queryForObject(String sql, RowMapper<T> rowMapper, Object... values) {
        return queryForObject(sql, createPreparedStatementSetter(values), rowMapper);
    }

    private T convertToObject(RowMapper<T> rowMapper, PreparedStatement pstmt) throws SQLException {
        T o = null;
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                o = rowMapper.mapRow(rs);
            }
        }
        return o;
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object... values) {
        return pstmt -> {
            for (int i = 0; i < values.length; i++) {
                pstmt.setString(i + 1, (String) values[i]);
            }
        };
    }

    private List<T> convertToList(RowMapper<T> rowMapper, PreparedStatement pstmt) throws SQLException {
        List<T> list = new ArrayList<>();
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(rowMapper.mapRow(rs));
            }
        }
        return list;
    }
}

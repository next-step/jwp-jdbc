package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public void update(String sql, PreparedStatementSetter pstmtSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("update 실행을 실패하였습니다.", e);
        }
    }

    public List<Object> query(String sql, PreparedStatementSetter pstmtSetter, RowMapper rowMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            return convertToList(rowMapper, pstmt);
        } catch (SQLException e) {
            throw new DataAccessException("query 실행을 실패하였습니다.", e);
        }
    }

    private List<Object> convertToList(RowMapper rowMapper, PreparedStatement pstmt) throws SQLException {
        List<Object> list = new ArrayList<>();
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(rowMapper.mapRow(rs));
            }
        }
        return list;
    }

    public Object queryForObject(String sql, PreparedStatementSetter pstmtSetter, RowMapper rowMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            return convertToObject(rowMapper, pstmt);
        } catch (SQLException e) {
            throw new DataAccessException("queryForObject 실행을 실패하였습니다.", e);
        }
    }

    private Object convertToObject(RowMapper rowMapper, PreparedStatement pstmt) throws SQLException {
        Object o = null;
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                o = rowMapper.mapRow(rs);
            }
        }
        return o;
    }
}

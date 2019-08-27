package next.dao.template;

import core.jdbc.ConnectionManager;
import next.dao.mapper.row.RowMapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class JdbcTemplate {
    public <T> T selectOne(String sql, RowMapper<T> rowMapper, Object... objects) throws SQLException, IllegalAccessException {
        ResultSet rs = null;
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            prepareValues(pstmt, objects);
            rs = pstmt.executeQuery();
            return rowMapper.mapResult(rs)
                    .stream()
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    public <T> List<T> selectAll(String sql, RowMapper<T> rowMapper) throws SQLException, IllegalAccessException {
        ResultSet rs = null;
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            rs = pstmt.executeQuery();
            return rowMapper.mapResult(rs);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    public void executeUpdate(String sql, Object... objects) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            prepareValues(pstmt, objects);
            pstmt.executeUpdate();
        }
    }

    private void prepareValues(PreparedStatement pstmt, Object... objects) throws SQLException {
        for (int i = 0; i < objects.length; i++) {
            pstmt.setObject(i + 1, objects[i]);
        }
    }
}

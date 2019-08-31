package next.dao.template;

import next.dao.mapper.row.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static core.jdbc.ConnectionManager.getConnection;

public class JdbcTemplate {
    public void executeUpdate(String sql, Object... objects) throws SQLException {
        try (Connection con = getConnection();
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

    public <T> T selectOne(String sql, RowMapper<T> rowMapper, Object... objects) throws SQLException, IllegalAccessException {
        ResultSet rs = null;
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            prepareValues(pstmt, objects);
            rs = pstmt.executeQuery();
            rs.next();
            return rowMapper.mapObject(rs);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    public <T> List<T> selectAll(String sql, RowMapper<T> rowMapper) throws SQLException, IllegalAccessException {
        ResultSet rs = null;
        List<T> results = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            rs = pstmt.executeQuery();

            while (rs.next()) {
                results.add(rowMapper.mapObject(rs));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return results;
    }
}

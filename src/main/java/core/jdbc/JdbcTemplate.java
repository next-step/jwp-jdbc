package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private Connection con;

    public JdbcTemplate() {
        con = ConnectionManager.getConnection();
    }

    public void update(String sql, Object... values) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValue(pstmt, values);
            pstmt.executeUpdate();
        }
    }

    public <T> T queryForObject(String sql, Object[] values, RowMapper<T> rowMapper) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValue(pstmt, values);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rowMapper.mapRow(rs, 0);
            }
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery())
        {
            List<T> results = new ArrayList<>();
            int i = 0;
            while (rs.next()) {
                results.add(rowMapper.mapRow(rs, i++));
            }
            return results;
        }
    }

    private void setValue(PreparedStatement pstmt, Object... objects) throws SQLException {
        for (int i = 0; i < objects.length; ++i) {
            pstmt.setObject(i+1, objects[i]);
        }
    }

}

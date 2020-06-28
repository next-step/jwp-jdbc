package core.jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public void update(String sql, Object... args) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            for (int i = 0; i < args.length; i++) {
                final Object value = args[i];

                if (value instanceof String) {
                    pstmt.setString(i + 1, (String) args[i]);
                }
                if (value instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) args[i]);
                }
            }

            pstmt.executeUpdate();
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int rowNum = 0;
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rowMapper.mapRow(rs, rowNum++));
            }

            return result;
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
<<<<<<< Updated upstream
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();) {
=======
             PreparedStatement pstmt = con.prepareStatement(sql)) {
>>>>>>> Stashed changes

            for (int i = 0; i < args.length; i++) {
                final Object value = args[i];

                if (value instanceof String) {
                    pstmt.setString(i + 1, (String) args[i]);
                }
                if (value instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) args[i]);
                }
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return rowMapper.mapRow(rs, 0);
            }
        }
    }
}

package core.jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public void update(String sql, Object... args) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);

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
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws SQLException {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rowMapper.mapRow(rs, 1));
            }

            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                final Object value = args[i];

                if (value instanceof String) {
                    pstmt.setString(i + 1, (String) args[i]);
                }
                if (value instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) args[i]);
                }
            }
            rs = pstmt.executeQuery();

            T result = null;
            if (rs.next()) {
                result = rowMapper.mapRow(rs, 1);
            }
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }
}

package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public void execute(String sql, Object... args) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {

                if (args[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (int) args[i]);
                }

                if (args[i] instanceof String) {
                    pstmt.setString(i + 1, (String) args[i]);
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

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {

                if (args[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (int) args[i]);
                }

                if (args[i] instanceof String) {
                    pstmt.setString(i + 1, (String) args[i]);
                }
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }
            return null;
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

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... args) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {

                if (args[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (int) args[i]);
                }

                if (args[i] instanceof String) {
                    pstmt.setString(i + 1, (String) args[i]);
                }
            }
            List<T> rows = new ArrayList<>();
            rs = pstmt.executeQuery();
            if (rs.next()) {
                rows.add(rowMapper.mapRow(rs));
            }
            return rows;
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

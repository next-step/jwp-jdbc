package core.jdbc;

import com.google.common.collect.Lists;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by iltaek on 2020/07/06 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class JdbcTemplate {

    public void update(String sql, Object... params) throws SQLException {
        update(sql, createPreparedStatementSetter(params));
    }

    public void update(String sql, PreparedStatementSetter pss) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            pss.setValue(pstmt);

            pstmt.executeUpdate();
        } finally {
            closeConnection(con, pstmt);
        }
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object[] params) {
        return pstmt -> {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        };
    }

    private void closeConnection(Connection con, PreparedStatement pstmt) throws SQLException {
        if (pstmt != null) {
            pstmt.close();
        }

        if (con != null) {
            con.close();
        }
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rm, Object... params) throws SQLException {
        return queryForList(sql, rm, createPreparedStatementSetter(params));
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rm, PreparedStatementSetter pss) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            pss.setValue(pstmt);

            rs = pstmt.executeQuery();

            return mapList(rs, rm);
        } finally {
            if (rs != null) {
                rs.close();
            }
            closeConnection(con, pstmt);
        }
    }

    private <T> List<T> mapList(ResultSet rs, RowMapper<T> rm) throws SQLException {
        List<T> results = Lists.newArrayList();
        while (rs.next()) {
            results.add(rm.mapRow(rs));
        }
        return results;
    }

    public <T> T queryForObject(String sql, RowMapper<T> rm, Object... params) throws SQLException {
        return queryForList(sql, rm, createPreparedStatementSetter(params)).get(0);
    }
}

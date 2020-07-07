package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created By kjs4395 on 2020-07-06
 */
public class JdbcTemplate<T> {
    public void insertOrUpdate(String sql, BindPrepareStatement bindPrepareStatement) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            bindPrepareStatement.setPrepareStatement(pstmt);

            pstmt.executeUpdate();
        } finally {
            this.closeConnection(con, pstmt);
        }
    }

    public void insertOrUpdate(String sql, Object... values) throws SQLException {
        BindPrepareStatement bindPrepareStatement = pstmt -> {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
        };
        insertOrUpdate(sql, bindPrepareStatement);
    }

    public T findByUserId(String sql, BindPrepareStatement bindPrepareStatement, BindResultSet bindResultSet) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            bindPrepareStatement.setPrepareStatement(pstmt);

            rs = pstmt.executeQuery();
            return (T) bindResultSet.bindResultSet(rs);
        } finally {
            closeConnection(con, pstmt);
            closeResultConnection(rs);
        }
    }

    public List<T> findAll(String sql, BindResultSet brs) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();

            return (List<T>) brs.bindResultSet(rs);

        } finally {
            closeConnection(con, pstmt);
            closeResultConnection(rs);
        }
    }

    private void closeConnection(Connection con, PreparedStatement pstmt) throws SQLException {
        if (pstmt != null) {
            pstmt.close();
        }

        if (con != null) {
            con.close();
        }
    }

    private void closeResultConnection(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }
}

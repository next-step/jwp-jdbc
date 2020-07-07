package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created By kjs4395 on 2020-07-06
 */
public class SelectJdbcTemplate <T> {
    public T findByUserId(String sql,BindPrepareStatement bindPrepareStatement, BindResultSet bindResultSet) throws SQLException {
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

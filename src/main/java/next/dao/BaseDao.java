package next.dao;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDao {
    protected void save(String sql, Object... parameters) {
        PreparedStatement pstmt = null;
        try {
            pstmt = getPreparedStatement(sql);
            setValues(pstmt, parameters);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataSQLException(e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                throw new DataSQLException(e);
            }
        }
    }

    private void setValues(PreparedStatement pstmt, Object... parameters) throws SQLException {
        int ind = 1;
        for (Object parameter : parameters) {
            pstmt.setString(ind++, (String) parameter);
        }
    }

    private PreparedStatement getPreparedStatement(String sql) throws SQLException {
        Connection con = ConnectionManager.getConnection();
        return con.prepareStatement(sql);
    }

    protected Object select(String sql, Mapper mapper, Object... parameters) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = getPreparedStatement(sql);
            setValues(pstmt, parameters);
            rs = pstmt.executeQuery();
            return mapper.map(rs);
        } catch (SQLException e) {
            throw new DataSQLException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                throw new DataSQLException(e);
            }
        }
    }
}

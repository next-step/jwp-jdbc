package core.jdbc;

import core.jdbc.exception.SqlExecuteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public void executeUpdate(String sql, PreparedStatementSetter preparedStatementSetter) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            preparedStatementSetter.setParameter(pstmt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SqlExecuteException(e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new SqlExecuteException(e);
            }
        }
    }

    public <T> T findById(String sql, PreparedStatementSetter preparedStatementSetter, ResultSetReader<T> resultSetReader) {
        List<T> objects = executeSelect(sql, preparedStatementSetter, resultSetReader);
        return objects.get(0);
    }

    public <T> List<T> findAll(String sql, PreparedStatementSetter preparedStatementSetter, ResultSetReader<T> resultSetReader) {
        return executeSelect(sql, preparedStatementSetter, resultSetReader);
    }

    private <T> List<T> executeSelect(String sql, PreparedStatementSetter preparedStatementSetter, ResultSetReader<T> resultSetReader) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            preparedStatementSetter.setParameter(pstmt);

            rs = pstmt.executeQuery();

            List<T> objects = new ArrayList<>();

            while (rs.next()) {
                T object = resultSetReader.read(rs);
                objects.add(object);
            }

            return objects;
        } catch (SQLException e) {
            throw new SqlExecuteException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new SqlExecuteException(e);
            }
        }
    }

}

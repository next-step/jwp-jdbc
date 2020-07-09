package core.jdbc;

import next.exception.JdbcTemplateCloseException;
import next.exception.QueryExecuteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created By kjs4395 on 2020-07-06
 */
public class JdbcTemplate<T> {
    public void insertOrUpdate(String sql, BindPrepareStatement bindPrepareStatement) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            bindPrepareStatement.setPrepareStatement(pstmt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new QueryExecuteException("insert Or update Query execute Exception!");
        } finally {
            this.closeConnection(con, pstmt);
        }
    }

    public void insertOrUpdate(String sql, Object... values) {
        BindPrepareStatement bindPrepareStatement = pstmt -> {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
        };
        insertOrUpdate(sql, bindPrepareStatement);
    }

    public T findById(String sql, BindResultSet bindResultSet, Object...values) {
        BindPrepareStatement bindPrepareStatement = pstmt -> {
            for(int i=0; i<values.length; i++) {
                pstmt.setObject(i+1, values[i]);
            }
        };
        return findById(sql, bindPrepareStatement,bindResultSet);
    }


    public T findById(String sql, BindPrepareStatement bindPrepareStatement, BindResultSet brs)  {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            bindPrepareStatement.setPrepareStatement(pstmt);

            rs = pstmt.executeQuery();
            T result = null;
            if(rs.next()) {
                result =  (T) brs.bindResultSet(rs);
            }
            return result;
        } catch (SQLException e) {
            throw new QueryExecuteException("find Query execute Exception!");
        } finally {
            closeConnection(con, pstmt);
            closeResultConnection(rs);
        }
    }

    public List<T> findAll(String sql, BindResultSet brs) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();

            return (List<T>) brs.bindResultSet(rs);

        } catch (SQLException e) {
            throw new QueryExecuteException("find all query execute Exception!");
        } finally {
            closeConnection(con, pstmt);
            closeResultConnection(rs);
        }
    }

    private void closeConnection(Connection con, PreparedStatement pstmt)  {
        try {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            throw new JdbcTemplateCloseException("connection or preparedStatement close Exception");
        }
    }

    private void closeResultConnection(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new JdbcTemplateCloseException("result set close Exception");
            }
        }
    }
}

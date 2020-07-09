package core.jdbc;

import next.exception.JdbcTemplateCloseException;
import next.exception.QueryExecuteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public T findById(String sql, BindResultSet bindResultSet, Object... values) {
        List<T> results = this.find(sql, selectQueryBindStatement(values), bindResultSet);

        if(results.isEmpty()) {
            throw new QueryExecuteException("result is null");
        }

        return results.get(0);
    }

    public List<T> findAll(String sql, BindResultSet brs, Object... values) {
        return find(sql, selectQueryBindStatement(values), brs);
    }

    private List<T> find(String sql, BindPrepareStatement bindPrepareStatement, BindResultSet brs) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            bindPrepareStatement.setPrepareStatement(pstmt);

            rs = pstmt.executeQuery();

            List<T> results = new ArrayList<>();

            if (rs.next()) {
                results.add((T) brs.bindResultSet(rs));
            }

            return results;

        } catch (SQLException e) {
            throw new QueryExecuteException("find query execute Exception!");
        } finally {
            closeConnection(con, pstmt);
            closeResultConnection(rs);
        }
    }

    private BindPrepareStatement selectQueryBindStatement(Object... values) {
        return pstmt -> {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
        };
    }

    private void closeConnection(Connection con, PreparedStatement pstmt) {
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

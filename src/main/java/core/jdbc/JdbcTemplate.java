package core.jdbc;

import core.jdbc.exception.SqlExecuteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public void insert(String sql, PreparedStatementSetter preparedStatementSetter) {
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

    public void update(String sql, PreparedStatementSetter preparedStatementSetter) {
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
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            preparedStatementSetter.setParameter(pstmt);

            rs = pstmt.executeQuery();

            T object = null;
            if (rs.next()) {
                object = resultSetReader.read(rs);
            }
            return object;
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

    public <T> List<T> findAll(String sql, PreparedStatementSetter preparedStatementSetter, ResultSetReader<T> resultSetReader) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            List<T> objects = new ArrayList<>();

            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            preparedStatementSetter.setParameter(pstmt);

            rs = pstmt.executeQuery();

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

package core.jdbc;

import next.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcTemplate {

    public static int executeUpdate(String sql, Object... objects) throws DataAccessException {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            setObjects(pstmt, objects);

            return pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    public static int executeUpdate(String sql, List<Object> list) throws DataAccessException {
        return executeUpdate(sql, list.toArray());
    }

    public static <T> T executeQuery(String sql, Object[] objects, RowMapper<T> mapper) throws DataAccessException {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            setObjects(pstmt, objects);

            try (ResultSet rs = pstmt.executeQuery()) {
                return mapper.mapping(rs);
            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    public static <T> T executeQuery(String sql, List<Object> list, RowMapper<T> mapper) throws DataAccessException {
        return executeQuery(sql, list.toArray(), mapper);
    }

    public static <T> T executeQuery(String sql, RowMapper<T> mapper) throws DataAccessException {
        return executeQuery(sql, new Object[0], mapper);
    }

    private static void setObjects(PreparedStatement pstmt, Object[] objects) throws DataAccessException {
        if (objects == null) {
            return;
        }
        try {
            for (int i = 0; i < objects.length; i++) {
                pstmt.setObject(i + 1, objects[i]);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException(ex);
        }

    }
}

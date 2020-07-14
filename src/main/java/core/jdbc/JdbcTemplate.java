package core.jdbc;

import core.jdbc.error.JdbcErrorType;
import core.jdbc.error.code.AbstractErrorCode;
import core.jdbc.error.code.H2ErrorCode;
import next.exception.DataAccessException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
            AbstractErrorCode error = new H2ErrorCode();
            if (error.isDuplicate(ex.getErrorCode())) {
                throw new DataAccessException(JdbcErrorType.DUPLICATE_KEY, ex);
            }
            if (error.isBadSqlGrammar(ex.getErrorCode())) {
                throw new DataAccessException(JdbcErrorType.BAD_SQL_GRAMMAR, ex);
            }
            if (error.isDataAccessResourceFailure(ex.getErrorCode())) {
                throw new DataAccessException(JdbcErrorType.DATA_ACCESS_RESOURCE_FAILURE, ex);
            }
            if (error.isDeadlockLoser(ex.getErrorCode())) {
                throw new DataAccessException(JdbcErrorType.DEADLOCK_LOSER, ex);
            }
            throw new DataAccessException(ex);
        }
    }

    public static int executeUpdate(String sql, List<Object> list) throws DataAccessException {
        return executeUpdate(sql, list.toArray());
    }

    public static <T> T executeQuery(String sql, RowMapper<T> mapper, Object... objects) throws DataAccessException {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            setObjects(pstmt, objects);

            try (ResultSet rs = pstmt.executeQuery()) {
                return mapper.mapRow(rs);
            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    public static <T> T executeQuery(String sql, List<Object> list, RowMapper<T> mapper) throws DataAccessException {
        return executeQuery(sql, mapper, list.toArray());
    }

    public static <T> T executeQuery(String sql, RowMapper<T> mapper) throws DataAccessException {
        return executeQuery(sql, mapper, new Object[0]);
    }

    public static <T> List<T> executeQuery(String sql, Class<T> type) throws DataAccessException {
        return executeQuery(sql, (rs) -> setResultListObjects(type, rs), new Object[0]);
    }

    public static <T> List<T> executeQuery(String sql, Class<T> type, Object... objects) throws DataAccessException {
        return executeQuery(sql, (rs) -> setResultListObjects(type, rs), objects);
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

    private static <T> List<T> setResultListObjects(Class<T> type, ResultSet rs) {
        List<T> dataList = new ArrayList<>();

        try {
            while (rs.next()) {
                T obj = setResultObject(type, rs);
                dataList.add(obj);
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }

        return dataList;
    }

    private static <T> T setResultObject(Class<T> type, ResultSet rs) throws Exception {
        T obj = type.newInstance();
        Field[] fields = type.getDeclaredFields();
        for (Field f : fields) {
            String fieldName = f.getName();

            setResultSetField(obj, f, rs.getObject(fieldName));
        }
        return obj;
    }

    private static void setResultSetField(Object obj, Field field, Object value) throws Exception {
        field.setAccessible(true);
        field.set(obj, value);
    }

}

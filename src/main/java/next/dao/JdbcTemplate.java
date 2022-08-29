package next.dao;

import core.jdbc.ConnectionManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public void update(String sql, Object... params) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstmt.setString(i + 1, String.valueOf(params[i]));
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("쿼리 실행을 실패하였습니다.", e);
        } finally {
            close(con, pstmt, null);
        }
    }

    public <T> T selectObject(String sql, Class<T> returnType, Object... params) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstmt.setString(i + 1, String.valueOf(params[i]));
            }

            rs = pstmt.executeQuery();
            return extractObject(rs, returnType);
        } catch (SQLException e) {
            throw new IllegalStateException("쿼리 실행을 실패하였습니다.", e);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new IllegalStateException("쿼리 결과 변환을 실패하였습니다.", e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    private <T> T extractObject(ResultSet rs, Class<T> returnType)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, SQLException {
        final Field[] fields = returnType.getDeclaredFields();
        final Constructor<T> constructor = returnType.getDeclaredConstructor();
        constructor.setAccessible(true);
        final T instance = constructor.newInstance();

        if (rs.next()) {
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(instance, rs.getString(field.getName()));
            }
        }
        return instance;
    }

    public <T> List<T> selectList(String sql, Class<T> returnType, Object... params) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstmt.setString(i + 1, String.valueOf(params[i]));
            }

            rs = pstmt.executeQuery();
            return extractList(returnType, rs);
        } catch (SQLException e) {
            throw new IllegalStateException("쿼리 실행을 실패하였습니다.", e);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new IllegalStateException("쿼리 결과 변환을 실패하였습니다.", e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    private <T> List<T> extractList(Class<T> returnType, ResultSet rs)
            throws NoSuchMethodException, SQLException, InstantiationException, IllegalAccessException, InvocationTargetException {
        final Field[] fields = returnType.getDeclaredFields();
        final Constructor<T> constructor = returnType.getDeclaredConstructor();
        constructor.setAccessible(true);
        final List<T> list = new ArrayList<>();

        while (rs.next()) {
            final T instance = constructor.newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(instance, rs.getString(field.getName()));
            }
            list.add(instance);
        }
        return list;
    }

    private void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new IllegalStateException("ReseltSet 닫기를 실패하였습니다.", e);
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                throw new IllegalStateException("PreparedStatement 닫기를 실패하였습니다.", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                throw new IllegalStateException("Connection 닫기를 실패하였습니다.", e);
            }
        }
    }
}

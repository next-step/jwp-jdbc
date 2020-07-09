package core.jdbc.custom;

import core.jdbc.ConnectionManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public abstract class UpdateJdbcTemplate<T> {

    private Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    private PreparedStatement getPreparedStatement(String sql) throws Exception {
        ActionablePrepared actionablePrepared = (connection) -> connection.prepareStatement(sql);
        return actionablePrepared.getPreparedStatement(getConnection());
    }

    public void update(T t) {
        try (PreparedStatement pstmt = getPreparedStatement(createQueryForUpdate())) {
            final Field[] fields = t.getClass().getDeclaredFields();
            final Object obj = findById(t, fields[0].get(t).toString());
            if (Objects.nonNull(obj)) {
                setValuesForUpdate(pstmt, obj);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private T findById(T t, final String id) {
        final Field[] fields = t.getClass().getDeclaredFields();
        fields[0].setAccessible(true);
        String tableName = t.getClass().getSimpleName().toUpperCase();
        String sql = String.format("SELECT * FROM %sS WHERE %s = ?", tableName, fields[0].getName());

        try (PreparedStatement pstmt = getPreparedStatement(sql)) {
            pstmt.setString(1, id);

            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return (T) getConstructor(t.getClass(), resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getConstructor(Class clazz, ResultSet resultSet) throws Exception {
        final Field[] fields = clazz.getDeclaredFields();
        Object[] values = new Object[fields.length];
        Class[] types = new Class[fields.length];
        for (int i = 1; i <= fields.length; i++) {
            values[i - 1] = resultSet.getString(i);
            types[i - 1] = fields[i - 1].getType();
        }
        Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(types);
        return declaredConstructor.newInstance(values);
    }

    public abstract String createQueryForUpdate();

    public abstract PreparedStatement setValuesForUpdate(PreparedStatement preparedStatement, Object object) throws IllegalAccessException, SQLException;
}

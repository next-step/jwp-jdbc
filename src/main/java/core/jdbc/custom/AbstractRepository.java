package core.jdbc.custom;

import core.jdbc.ConnectionManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepository implements Repository<Object, String> {

    private Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    private PreparedStatement getPreparedStatement(String sql) {
        try {
            return getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(final Object object) {
        final Field[] fields = object.getClass()
                .getDeclaredFields();

        String marks = getQuestionMark(fields.length);
        String tableName = object.getClass().getSimpleName().toUpperCase();
        String sql = String.format("INSERT INTO %sS VALUES (%s)", tableName, marks);

        try (PreparedStatement pstmt = getPreparedStatement(sql)) {
            for (int i = 1; i <= fields.length; i++) {
                fields[i - 1].setAccessible(true);
                pstmt.setString(i, fields[i - 1].get(object).toString());
            }
            pstmt.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object findById(final Class clazz, final String s) {
        final Field[] fields = clazz.getDeclaredFields();
        fields[0].setAccessible(true);
        String tableName = clazz.getSimpleName().toUpperCase();
        String sql = String.format("SELECT * FROM %sS WHERE %s = ?", tableName, fields[0].getName());

        try (PreparedStatement pstmt = getPreparedStatement(sql)) {
            pstmt.setString(1, s);

            ResultSet resultSet = pstmt.executeQuery();
            Class[] types = new Class[fields.length];
            if (resultSet.next()) {
                Object[] objects = new Object[fields.length];
                for (int i = 1; i <= fields.length; i++) {
                    objects[i - 1] = resultSet.getString(i);
                    types[i - 1] = fields[i - 1].getType();
                }
                Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(types);
                return declaredConstructor.newInstance(objects);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(final Object object) {
        final Field[] fields = object.getClass().getDeclaredFields();
        fields[0].setAccessible(true);
        try {
            final Object obj = findById(object.getClass(), fields[0].get(object).toString());
            String tableName = obj.getClass().getSimpleName().toUpperCase();
            String setQuery = getUpdateSetQuestionMark(obj.getClass());
            String sql = String.format("UPDATE %sS SET %s WHERE %s = ?", tableName, setQuery, fields[0].getName());

            try (PreparedStatement pstmt = getPreparedStatement(sql)) {
                for (int i = 1; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    pstmt.setString(i, fields[i].get(object).toString());
                }
                fields[0].setAccessible(true);
                pstmt.setString(fields.length, fields[0].get(object).toString());
                pstmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private String getQuestionMark(int size) {
        StringBuilder questionMarks = new StringBuilder();
        for (int i = 0; i < size - 1; i++) {
            questionMarks.append("?, ");
        }
        questionMarks.append("?");
        return questionMarks.toString();
    }

    private String getUpdateSetQuestionMark(Class clazz) {
        List<String> mark = new ArrayList<>();
        final Field[] fields = clazz.getDeclaredFields();
        for (int i = 1; i < fields.length; i++) {
            mark.add(String.format("%s = ?", fields[i].getName()));
        }
        return String.join(",", mark);
    }
}

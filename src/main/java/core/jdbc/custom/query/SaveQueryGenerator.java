package core.jdbc.custom.query;

import core.jdbc.ConnectionManager;
import core.jdbc.custom.ActionablePrepared;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveQueryGenerator<T> implements QueryGenerator {

    T t;

    public SaveQueryGenerator(final T t) {
        this.t = t;
    }

    private Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    private PreparedStatement getPreparedStatement(String sql) throws Exception {
        ActionablePrepared actionablePrepared = (connection) -> connection.prepareStatement(sql);
        return actionablePrepared.getPreparedStatement(getConnection());
    }

    @Override
    public String make() {
        final Field[] fields = t.getClass().getDeclaredFields();
        fields[0].setAccessible(true);
        String tableName = t.getClass().getSimpleName().toUpperCase();
        try {
            String sql = String.format("SELECT * FROM %sS WHERE %s = ?", t.getClass().getSimpleName().toUpperCase(), fields[0].get(t));
            final PreparedStatement statement = getPreparedStatement(sql);
            statement.setString(1, fields[0].get(t).toString());
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String id = Arrays.stream(t.getClass().getDeclaredFields())
                        .map(Field::getName)
                        .findFirst()
                        .get();

                return String.format("UPDATE %sS SET %s WHERE %s = ?", tableName, getUpdateSetQuestionMark(t.getClass()), id);
            }
            return String.format("INSERT INTO %sS VALUES (%s)", tableName, getQuestionMark(t.getClass().getDeclaredFields().length));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

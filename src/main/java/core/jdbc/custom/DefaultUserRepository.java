package core.jdbc.custom;

import next.model.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DefaultUserRepository {

    private JdbcTemplate<User> jdbcTemplate;

    public DefaultUserRepository() {
        this.jdbcTemplate = new JdbcTemplate<>();
    }

    public void save(final User user) {
        jdbcTemplate.save(() -> createQuery(user), preparedStatement -> this.setValueBySave(user, preparedStatement));
    }


    public User findById(final String id) {
        return this.jdbcTemplate.queryForObject(() -> String.format("SELECT * FROM %s WHERE %s = ?", "USERS", id), preparedStatement -> {
            try {
                preparedStatement.setString(1, id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, this::createRow);
    }

    public List<User> findAll() {
        return this.jdbcTemplate.query(() -> String.format("SELECT * FROM %s", "USERS"), this::createRow);
    }


    private User createRow(final ResultSet resultSet) {
        try {
            final Field[] fields = User.class.getDeclaredFields();
            Object[] values = new Object[fields.length];
            Class[] types = new Class[fields.length];
            for (int i = 1; i <= fields.length; i++) {
                values[i - 1] = resultSet.getString(i);
                types[i - 1] = fields[i - 1].getType();
            }
            Constructor<?> declaredConstructor = User.class.getDeclaredConstructor(types);
            return (User) declaredConstructor.newInstance(values);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setValueBySave(User user, PreparedStatement preparedStatement) {
        final Field[] fields = user.getClass().getDeclaredFields();
        fields[0].setAccessible(true);
        try {
            final Object obj = findById(fields[0].get(user).toString());
            if (Objects.isNull(obj)) {
                for (int i = 1; i <= fields.length; i++) {
                    fields[i - 1].setAccessible(true);
                    preparedStatement.setString(i, fields[i - 1].get(user).toString());
                }
                return;
            }

            for (int i = 1; i < fields.length; i++) {
                fields[i].setAccessible(true);
                preparedStatement.setString(i, fields[i].get(user).toString());
            }
            fields[0].setAccessible(true);
            preparedStatement.setString(fields.length, fields[0].get(user).toString());

        } catch (IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
    }



    private String createQuery(User user) {
        final Field[] fields = user.getClass().getDeclaredFields();
        fields[0].setAccessible(true);
        String tableName = user.getClass().getSimpleName().toUpperCase();
        final Object obj;
        try {
            obj = findById(fields[0].get(user).toString());
            if (Objects.isNull(obj)) {
                return String.format("INSERT INTO %sS VALUES (%s)", tableName, getQuestionMark(user.getClass().getDeclaredFields().length));
            }

            String id = Arrays.stream(user.getClass().getDeclaredFields())
                    .map(Field::getName)
                    .findFirst()
                    .get();

            return String.format("UPDATE %sS SET %s WHERE %s = ?", tableName, getUpdateSetQuestionMark(user.getClass()), id);

        } catch (IllegalAccessException e) {
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

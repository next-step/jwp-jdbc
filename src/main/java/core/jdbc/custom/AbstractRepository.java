package core.jdbc.custom;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AbstractRepository<T, V> implements Repository<T, V> {

    private T t;

    AbstractRepository(T t) {
        this.t = t;
    }

    @Override
    public void save(final T t) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            final Field[] fields = t.getClass().getDeclaredFields();

            @Override
            public void setValues(final PreparedStatement preparedStatement) {
                fields[0].setAccessible(true);
                try {
                    final Object obj = findById((V) fields[0].get(t).toString());
                    if (Objects.isNull(obj)) {
                        for (int i = 1; i <= fields.length; i++) {
                            fields[i - 1].setAccessible(true);
                            preparedStatement.setString(i, fields[i - 1].get(t).toString());
                        }
                        return;
                    }

                    for (int i = 1; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                        preparedStatement.setString(i, fields[i].get(t).toString());
                    }
                    fields[0].setAccessible(true);
                    preparedStatement.setString(fields.length, fields[0].get(t).toString());

                } catch (IllegalAccessException | SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Object mapRow(final ResultSet resultSet) {
                return null;
            }
        };
        jdbcTemplate.save(createQuery(t));
    }

    public String createQuery(T t) {
        final Field[] fields = t.getClass().getDeclaredFields();
        fields[0].setAccessible(true);
        String tableName = t.getClass().getSimpleName().toUpperCase();
        final Object obj;
        try {
            obj = findById((V) fields[0].get(t).toString());
            if (Objects.isNull(obj)) {
                return String.format("INSERT INTO %sS VALUES (%s)", tableName, getQuestionMark(t.getClass().getDeclaredFields().length));
            }

            String id = Arrays.stream(t.getClass().getDeclaredFields())
                    .map(Field::getName)
                    .findFirst()
                    .get();

            return String.format("UPDATE %sS SET %s WHERE %s = ?", tableName, getUpdateSetQuestionMark(t.getClass()), id);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getQuestionMark(int size) {
        System.out.println(size);
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

    @Override
    public T findById(final V v) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(final PreparedStatement preparedStatement) {
                try {
                    preparedStatement.setString(1, (String) v);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Object mapRow(final ResultSet resultSet) {
                try {
                    final Field[] fields = t.getClass().getDeclaredFields();
                    Object[] values = new Object[fields.length];
                    Class[] types = new Class[fields.length];
                    for (int i = 1; i <= fields.length; i++) {
                        values[i - 1] = resultSet.getString(i);
                        types[i - 1] = fields[i - 1].getType();
                    }
                    Constructor<?> declaredConstructor = t.getClass().getDeclaredConstructor(types);
                    return declaredConstructor.newInstance(values);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        final Field[] fields = t.getClass().getDeclaredFields();
        fields[0].setAccessible(true);
        String tableName = t.getClass().getSimpleName().toUpperCase();
        String sql = String.format("SELECT * FROM %sS WHERE %s = ?", tableName, fields[0].getName());
        return (T) jdbcTemplate.queryForObject(sql);
    }

    @Override
    public List<T> findAll() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(final PreparedStatement preparedStatement) {

            }

            @Override
            public Object mapRow(final ResultSet resultSet) {

                try {
                    final Field[] fields = t.getClass().getDeclaredFields();
                    Object[] values = new Object[fields.length];
                    Class[] types = new Class[fields.length];
                    for (int i = 1; i <= fields.length; i++) {
                        values[i - 1] = resultSet.getString(i);
                        types[i - 1] = fields[i - 1].getType();
                    }
                    Constructor<?> declaredConstructor = t.getClass().getDeclaredConstructor(types);
                    return declaredConstructor.newInstance(values);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        String tableName = t.getClass().getSimpleName().toUpperCase();
        String sql = String.format("SELECT * FROM %sS", tableName);

        return (List<T>) jdbcTemplate.query(sql);
    }
}

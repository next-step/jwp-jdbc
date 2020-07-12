package core.jdbc.custom;

import next.model.User;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DefaultUserRepository {

    private JdbcTemplate<User> jdbcTemplate;
    private RowMapper<User> defaultRowMapper;

    public DefaultUserRepository() {
        this.jdbcTemplate = new JdbcTemplate<>();
        this.defaultRowMapper = new DefaultRowMapper<>(User.class);
    }

    public void save(final User user) {
        jdbcTemplate.save(() -> createQuery(user), new CreatePreparedStatementSetter<>(user));
    }


    public User findById(final String id) {
        return this.jdbcTemplate.queryForObject(() -> String.format("SELECT * FROM %s WHERE %s = ?", "USERS", id), preparedStatement -> {
            try {
                preparedStatement.setString(1, id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, defaultRowMapper);
    }

    public List<User> findAll() {
        return this.jdbcTemplate.query(() -> String.format("SELECT * FROM %s", "USERS"), defaultRowMapper);
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

package next.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import core.jdbc.ConnectionManager;

public class JdbcTemplate {

    public <T> T getOne(String query, RowMapper<T> rowMapper, Object... params) {
        var results = query(query, rowMapper, params);
        validate(results);

        return results.get(0);
    }

    public <T> List<T> getAll(String query, RowMapper<T> rowMapper, Object... params) {
        return query(query, rowMapper, params);
    }

    private <T> List<T> query(String query, RowMapper<T> rowMapper, Object[] params) {
        try (var connection = getConnection();
             var preparedStatement = createPreparedStatement(connection, query, params);
             var resultSet = preparedStatement.executeQuery();
        ) {
            List<T> results = new ArrayList<>();

            while (resultSet.next()) {
                results.add(rowMapper.mapping(resultSet));
            }

            return results;
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외가 발생했습니다. " + e.getMessage());
        }
    }

    public void executeUpdate(String query, Object... params) {
        try (var connection = getConnection();
             var preparedStatement = createPreparedStatement(connection, query, params);
        ) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외가 발생했습니다. " + e.getMessage());
        }
    }

    private Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    private PreparedStatement createPreparedStatement(Connection connection, String query, Object[] params) throws
        SQLException {
        var preparedStatement = connection.prepareStatement(query);

        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }

        return preparedStatement;
    }

    private <T> void validate(List<T> results) {
        if (results.size() == 0) {
            throw new IllegalArgumentException("쿼리결과가 없습니다");
        }

        if (results.size() > 1) {
            throw new IllegalArgumentException("쿼리결과가 2건 이상있습니다");
        }
    }

    public <T> T getOne(String query, Class<T> clazz, String userId) {
        return getOne(query, createRowMapper(clazz), userId);
    }

    public <T> List<T> getAll(String query, Class<T> clazz, Object... params) {
        return getAll(query, createRowMapper(clazz), params);
    }

    private <T> RowMapper<T> createRowMapper(Class<T> clazz) {
        var declaredConstructor = getDeclaredConstructor(clazz);
        var fields = clazz.getDeclaredFields();

        return resultSet -> {
            Object object = createObject(declaredConstructor);
            for (Field field : fields) {
                field.setAccessible(true);
                setField(resultSet, object, field);
            }
            return (T)object;
        };
    }

    private Constructor<?> getDeclaredConstructor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("기본생성자를 찾을 수 없습니다.");
        }
    }

    private Object createObject(Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("객체를 생성할 수 없습니다" + constructor);
        }
    }

    private void setField(ResultSet resultSet, Object object, Field field) throws SQLException {
        try {
            field.set(object, resultSet.getString(field.getName()));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("field를 수정할 수 없습니다" + field);
        }
    }
}

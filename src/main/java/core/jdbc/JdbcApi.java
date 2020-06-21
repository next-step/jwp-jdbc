package core.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static core.jdbc.ConnectionManager.getConnection;

public class JdbcApi<T> {
    public final Class<T> clazz;

    public JdbcApi(Class<T> clazz) {
        this.clazz = clazz;
    }

    private List<T> executeQuery(final String sql, final Object... values) {
        try(Connection connection = getConnection()) {
            PreparedStatement preparedStatement = prepareStatement(connection, sql);
            setValues(preparedStatement, values);
            ResultSet resultSet = executeQuery(preparedStatement);

            return convertToClasses(resultSet);
        } catch (Exception e) {
            throw new JdbcApiException("Something wrong");
        }
    }

    public T findOne(final String sql, final Object... values) {
        List<T> classes = executeQuery(sql, values);
        validUnique(classes);

        return classes.isEmpty() ? null : classes.get(0);
    }

    private void validUnique(List<T> classes) {
        if (classes.size() > 1) {
            throw new IllegalArgumentException("Query result not unique");
        }
    }

    private List<T> convertToClasses(ResultSet resultSet) throws SQLException {
        List<T> classes = new ArrayList<>();

        if (resultSet.next()) {
            classes.add(convert(resultSet));
        }

        return classes;
    }

    private T convert(ResultSet resultSet) {



        return null;
    }

    public List<T> findAll(final String sql, final Object... values) throws SQLException {
        return executeQuery(sql, values);
    }

    public void execute(final String sql, final Object... values) {
        try(Connection connection = getConnection()) {
            PreparedStatement preparedStatement = prepareStatement(connection, sql);
            setValues(preparedStatement, values);
            preparedStatement.execute();
        } catch (Exception e) {
            throw new JdbcApiException("Something wrong");
        }
    }

    private <T> T convertToTargetClass(ResultSet resultSet) {
        try {
            System.out.println(clazz);
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            return (T) declaredConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            if (resultSet.next()) {
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    private ResultSet executeQuery(PreparedStatement preparedStatement) {
        try {
            return preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throw new JdbcApiException("Fail to execute query : " + throwables.getMessage());
        }
    }

    private void setValues(PreparedStatement preparedStatement, Object... values) throws SQLException {
        for (int idx = 0 ; idx < values.length ; ++idx) {
            preparedStatement.setObject(idx + 1, values[idx]);
        }
    }

    private PreparedStatement prepareStatement(Connection connection, String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException throwables) {
            throw new JdbcApiException("Fail to connect to db server : " + throwables.getMessage());
        }
    }
}

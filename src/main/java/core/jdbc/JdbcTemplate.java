package core.jdbc;

import support.exception.DuplicatedEntityException;
import support.exception.ParameterClassNotFoundException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    private JdbcTemplate() {

    }

    public static JdbcTemplate getInstance() {
        return jdbcTemplate;
    }

    public void execute(String sql, Object... parameters) {
        Connection connection = ConnectionManager.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            this.setParameters(preparedStatement, parameters);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Object querySingle(String sql, Class<?> resultClass, Object... parameters) {
        Connection connection = ConnectionManager.getConnection();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            this.setParameters(preparedStatement, parameters);

            resultSet = preparedStatement.executeQuery();
            List<Object> results = this.convertResultSetToObjects(resultSet, resultClass);

            preparedStatement.close();
            connection.close();

            return this.getSingleObjectFromResults(results);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> query(String sql, Class<?> resultClass) {
        Connection connection = ConnectionManager.getConnection();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            List<Object> results = this.convertResultSetToObjects(resultSet, resultClass);

            preparedStatement.close();
            connection.close();

            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getSingleObjectFromResults(List<Object> results) {
        if (results.isEmpty()) {
            return null;
        }

        if (results.size() == 1) {
            return results.stream()
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

        throw new DuplicatedEntityException();
    }

    private void setParameters(PreparedStatement preparedStatement, Object... parameters) throws SQLException {
        int index = 0;
        for (Object parameter : parameters) {
            this.setParameter(index, parameter, preparedStatement);
            index++;
        }
    }

    private void setParameter(int index, Object parameter, PreparedStatement preparedStatement) throws SQLException {
        Class<?> parameterClass = parameter.getClass();
        if (parameterClass.equals(int.class)) {
            preparedStatement.setInt(index + 1, Integer.parseInt((String) parameter));
            return;
        }

        if (parameterClass.equals(String.class)) {
            preparedStatement.setString(index + 1, String.valueOf(parameter));
            return;
        }

        // TODO 다른 종류 class 들에 대한 분기처리
        throw new ParameterClassNotFoundException();
    }

    private List<Object> convertResultSetToObjects(ResultSet resultSet, Class<?> resultClazz) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Object> results = new ArrayList<>();
        while (resultSet.next()) {
            Object resultObject = this.getResultObject(resultClazz, resultSet);
            results.add(resultObject);
        }

        return results;
    }

    private Object getResultObject(Class<?> resultClazz, ResultSet resultSet) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        Object result = resultClazz.getConstructor().newInstance();
        Field[] fields = resultClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(result, this.getValueFromResultSet(resultSet, field.getName(), field.getType()));
        }

        return result;
    }

    private Object getValueFromResultSet(ResultSet resultSet, String filedName, Class<?> fieldType) throws SQLException {
        if (fieldType.equals(String.class)) {
            return resultSet.getString(filedName);
        }

        if (fieldType.equals(int.class)) {
            return resultSet.getInt(filedName);
        }

        // TODO 다른 종류 class 들에 대한 분기처리
        return null;
    }

}

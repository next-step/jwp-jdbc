package core.jdbc;

import support.exception.DuplicatedEntityException;
import support.exception.ParameterClassNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplateV1 {

    private static final JdbcTemplateV1 JDBC_TEMPLATE_V_1 = new JdbcTemplateV1();
    private final RowMapper rowMapper = new RowMapperImpl();

    private JdbcTemplateV1() {

    }

    public static JdbcTemplateV1 getInstance() {
        return JDBC_TEMPLATE_V_1;
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

    public <T> T querySingle(String sql, Class<?> resultClass, Object... parameters) {
        Connection connection = ConnectionManager.getConnection();
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            this.setParameters(preparedStatement, parameters);

            resultSet = preparedStatement.executeQuery();
            List<Object> results = this.convertResultSetToObjects(resultSet, resultClass);

            connection.close();
            return this.getSingleObjectFromResults(results);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> query(String sql, Class<?> resultClass) {
        Connection connection = ConnectionManager.getConnection();
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            resultSet = preparedStatement.executeQuery();
            List<T> results = this.convertResultSetToObjects(resultSet, resultClass);

            connection.close();
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T getSingleObjectFromResults(List<Object> results) {
        if (results.isEmpty()) {
            return null;
        }

        if (results.size() == 1) {
            return (T) results.stream()
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

    private <T> List<T> convertResultSetToObjects(ResultSet resultSet, Class<?> resultClazz) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<T> results = new ArrayList<>();
        while (resultSet.next()) {
            T resultObject = this.rowMapper.getResultFromRow(resultClazz, resultSet);
            results.add(resultObject);
        }

        return results;
    }

}

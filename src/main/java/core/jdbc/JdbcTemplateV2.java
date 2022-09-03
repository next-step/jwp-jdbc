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

public class JdbcTemplateV2 {

    private static final JdbcTemplateV2 jdbcTemplateV2 = new JdbcTemplateV2();

    private JdbcTemplateV2() {

    }

    public static JdbcTemplateV2 getInstance() {
        return jdbcTemplateV2;
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

    public <T> T querySingle(String sql, RowMapperV2<T> rowMapper, Object... parameters) {
        Connection connection = ConnectionManager.getConnection();
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            this.setParameters(preparedStatement, parameters);

            resultSet = preparedStatement.executeQuery();
            List<T> results = this.convertResultSetToObjects(resultSet, rowMapper);

            connection.close();
            return this.getSingleObjectFromResults(results);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> query(String sql, RowMapperV2<T> rowMapper) {
        Connection connection = ConnectionManager.getConnection();
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            resultSet = preparedStatement.executeQuery();
            List<T> results = this.convertResultSetToObjects(resultSet, rowMapper);

            connection.close();
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private <T> List<T> convertResultSetToObjects(ResultSet resultSet, RowMapperV2<T> rowMapper) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<T> results = new ArrayList<>();
        while (resultSet.next()) {
            T resultObject = rowMapper.map(resultSet);
            results.add(resultObject);
        }

        return results;
    }

    private <T> T getSingleObjectFromResults(List<T> results) {
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
}

package next.dao;

import core.jdbc.ConnectionManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JdbcTemplate<T> {

    public void insert(String sql, List<JdbcParameter> parameters) {
        execute(sql, parameters);
    }

    public void update(String sql, List<JdbcParameter> parameters) {
        execute(sql, parameters);
    }

    private void execute(String sql, List<JdbcParameter> parameters) {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            initParameters(parameters, preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public T findOne(String sql, List<JdbcParameter> parameters, Class<T> type) {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = initParameters(parameters, preparedStatement).executeQuery()) {

            if (rs.next()) {
                T newInstance = type.getDeclaredConstructor().newInstance();
                Field[] declaredFields = type.getDeclaredFields();

                setFields(rs, newInstance, declaredFields);

                return newInstance;
            }
            throw new IllegalArgumentException("대상을 찾을수 없습니다.");
        } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private PreparedStatement initParameters(List<JdbcParameter> parameters, PreparedStatement preparedStatement) throws SQLException {
        for (JdbcParameter parameter : parameters) {
            preparedStatement.setObject(parameter.getIndex(), parameter.getValue());
        }
        return preparedStatement;
    }

    public List<T> findAll(String sql, Class<T> type) {
        List<T> results = new ArrayList<>();
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                T newInstance = type.getDeclaredConstructor().newInstance();
                Field[] declaredFields = type.getDeclaredFields();

                setFields(rs, newInstance, declaredFields);
                results.add(newInstance);
            }

            return results;
        } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFields(ResultSet rs, T newInstance, Field[] declaredFields) throws SQLException, IllegalAccessException {
        for (Field field : declaredFields) {
            field.setAccessible(true);
            field.set(newInstance, rs.getString(field.getName()));
        }
    }
}

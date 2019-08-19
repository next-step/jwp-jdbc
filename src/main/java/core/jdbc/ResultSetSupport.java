package core.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetSupport {

    private ResultSet resultSet;

    public ResultSetSupport(ResultSet rs) {
        this.resultSet = rs;
    }

    public <T> List<T> getResults(RowMapper<T> rowMapper) throws SQLException {
        List<T> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(rowMapper.apply(resultSet));
        }
        return result;
    }

    public <T> T getResult(Class<T> clazz) throws SQLException {
        ResultSetRow<T> rsRow = new ResultSetRow<>(clazz);
        return createRow(resultSet, clazz, rsRow);
    }

    public <T> List<T> getResults(Class<T> clazz) throws SQLException {
        ResultSetRow<T> rsRow = new ResultSetRow<>(clazz);
        List<T> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(createRow(resultSet, clazz, rsRow));
        }
        return result;
    }

    private <T> T createRow(ResultSet resultSet, Class<T> clazz, ResultSetRow<T> rsRow) throws SQLException {
        try {
            T row = clazz.getConstructor().newInstance();
            rsRow.populateRow(row, resultSet);
            return row;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(clazz.getName() + " instantiate failed", e);
        }
    }

}

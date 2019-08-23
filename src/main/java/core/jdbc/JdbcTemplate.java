package core.jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate implements JdbcOperations {

    private static final int START_PARAMETER_NUMBER = 1;
    public static final int ONE_OBJECT = 0;

    @Override
    public void execute(String sql, Object... parameters) {
        execute(PreparedStatement::executeUpdate, sql, parameters);
    }

    @Override
    public <T> T queryForObject(String sql, ResultSetExtractor<T> resultSetExtractor, Object... parameters) {
        return queryForList(sql, resultSetExtractor, parameters).get(ONE_OBJECT);
    }

    @Override
    public <T> List<T> queryForList(String sql, ResultSetExtractor<T> resultSetExtractor, Object... parameters) {
        return query(sql, resultSetExtractor, parameters);
    }

    <R> R execute(StatementCallback<R> callback, String sql, Object... parameters) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            setValues(ps, parameters);

            return callback.perform(ps);
        } catch (SQLException e) {
            throw new JdbcExecuteException(e);
        }
    }

    <R> List<R> query(String sql, ResultSetExtractor<R> resultSetExtractor, Object... parameters) {
        return execute(ps -> {
            ResultSet rs = ps.executeQuery();
            List<R> results = new ArrayList<>();
            while (rs.next()) {
                results.add(resultSetExtractor.extract(rs));
            }
            return results;
        }, sql, parameters);
    }

    private void setValues(PreparedStatement ps, Object[] parameters) throws SQLException {
        int i = START_PARAMETER_NUMBER;
        for (Object obj : parameters) {
            ps.setString(i++, obj.toString());
        }
    }
}
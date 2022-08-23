package core.jdbc;

import core.jdbc.exception.JdbcTemplateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {

    private JdbcTemplate() {
    }

    public static JdbcTemplate getInstance() {
        return JdbcTemplateHolder.INSTANCE;
    }

    public int execute(final String sql, final Object... arguments) {
        return execute(sql, getPreparedStatementSetter(arguments));
    }

    public int execute(final String sql, final PreparedStatementSetter setter) {
        final PreparedStatementCreator creator = getPreparedStatementCreator(sql, setter);

        try (
            final Connection con = ConnectionManager.getConnection();
            final Transaction transaction = new Transaction(con);
            final PreparedStatement preparedStatement = creator.createPreparedStatement(con)
        ) {

            final int result = preparedStatement.executeUpdate();
            transaction.commit();

            return result;
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    private PreparedStatementCreator getPreparedStatementCreator(final String sql, final PreparedStatementSetter setter) {
        return new DefaultPreparedStatementCreator(sql, setter);
    }

    public <T> Optional<T> queryForObject(final String sql, final RowMapperFunction<T> function, final Object... arguments) {
        return query(sql, rs -> {
            if (rs.next()) {
                return Optional.of(function.apply(rs));
            }

            return Optional.empty();
        }, arguments);
    }

    public <T> List<T> queryForList(final String sql, final RowMapperFunction<T> function, final Object... arguments) {
        return query(sql, rs -> {
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(function.apply(rs));
            }

            return results;
        }, arguments);
    }

    private <T> T query(final String sql, final QueryFunction<T> function, final Object... arguments) {
        return query(sql, function, getPreparedStatementSetter(arguments));
    }

    private PreparedStatementSetter getPreparedStatementSetter(final Object[] arguments) {
        return new DefaultPreparedStatementSetter(arguments);
    }

    private <T> T query(final String sql, final QueryFunction<T> function, final PreparedStatementSetter setter) {
        final PreparedStatementCreator creator = getPreparedStatementCreator(sql, setter);

        try (
            final Connection con = ConnectionManager.getConnection();
            final PreparedStatement preparedStatement = creator.createPreparedStatement(con);
            final ResultSet rs = preparedStatement.executeQuery()
        ) {
            con.setReadOnly(true);

            return function.apply(rs);
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    private static class JdbcTemplateHolder {
        private static final JdbcTemplate INSTANCE = new JdbcTemplate();
    }
}

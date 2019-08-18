package core.jdbc;

import core.exception.JdbcException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> selectList(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) {
        return execute(sql, new SelectQueryAction(), new ParameterSetter(args, argTypes), new ListRowMapper<T>(rowMapper));
    }

    public <T> T select(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) {
        return execute(sql, new SelectQueryAction(), new ParameterSetter(args, argTypes), new SingleRowMapper<T>(rowMapper));
    }

    public int update(String sql, Object[] args, int[] argTypes) {
        return execute(sql, new UpdateQueryAction(), new ParameterSetter(args, argTypes), new RowCountMapper());
    }

    private<T, R> R execute(String sql, QueryAction<T> queryAction, ParameterSetter parameterSetter, ResultMapper<T, R> resultMapper) {
        try(Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
            ) {

            setArgsIfSupport(preparedStatement, parameterSetter);
            T result = queryAction.action(preparedStatement);

            return resultMapper.resultMapping(result);
        } catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    private void setArgsIfSupport(PreparedStatement preparedStatement, ParameterSetter parameterSetter) throws SQLException {
        if(parameterSetter != null) {
            parameterSetter.setArgs(preparedStatement);
        }
    }
}

package core.jdbc;

import core.jdbc.callback.QueryStatementCallback;
import core.jdbc.callback.StatementCallback;
import core.jdbc.callback.UpdateStatementCallback;
import core.jdbc.exception.SqlRunTimeException;
import core.jdbc.resultset.ResultSetExtractor;
import core.jdbc.resultset.RowMapper;
import core.jdbc.resultset.RowMapperResultSetExtractor;
import core.util.StringUtils;
import lombok.Getter;
import next.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Objects;

public class JdbcTemplate {
    @Getter
    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int update(String sql, Object... args) {
        return execute(new UpdateStatementCallback(sql), args);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return query(sql, new RowMapperResultSetExtractor<>(rowMapper));
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
        List<T> results = query(sql, new RowMapperResultSetExtractor<>(rowMapper));
        return getSingleResult(results);
    }

    private <T> T getSingleResult(List<T> results) {
        if (CollectionUtils.isEmpty(results)) {
            throw new SqlRunTimeException("Empty Result");
        }

        return results.stream().findFirst().get();
    }

    private <T> T query(final String sql, final ResultSetExtractor<T> rse) throws SqlRunTimeException {
        if (StringUtils.isEmpty(sql) || Objects.isNull(rse)) {
            throw new IllegalArgumentException();
        }

        return execute(new QueryStatementCallback<T>(sql, rse));
    }

    private <T> T execute(StatementCallback<T> qsc, Object... args) throws SqlRunTimeException {
        if (Objects.isNull(qsc)) {
            throw new IllegalArgumentException();
        }

        try(Connection connection = this.dataSource.getConnection();
            Statement preparedStatement = connection.createStatement()) {
            T result = qsc.executeStatement(preparedStatement);
            return result;
        }
        catch (SQLException e) {
            throw new SqlRunTimeException();
        }
    }
}

package core.jdbc;

import core.jdbc.callback.QueryStatementCallback;
import core.jdbc.callback.StatementCallback;
import core.jdbc.callback.UpdateStatementCallback;
import core.jdbc.exception.SqlRunTimeException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class JdbcTemplate {
    @Getter
    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int update(String sql, Object... args) {
        return execute(new UpdateStatementCallback(sql, new ArgumentPreparedStatementSetter(args)));
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object ...args) {
        return query(sql, args, new RowMapperResultSetExtractor<>(rowMapper));
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return query(sql, null, new RowMapperResultSetExtractor<>(rowMapper));
    }

    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
        return query(sql, args, new RowMapperResultSetExtractor<>(rowMapper));
    }

    private <T> List<T> query(String sql, Object[] args, RowMapperResultSetExtractor<T> rse) {
        return (List<T>) execute(new QueryStatementCallback(sql, new ArgumentPreparedStatementSetter(args), rse));
    }

    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) {
        return getSingleResult(query(sql, args, new RowMapperResultSetExtractor<>(rowMapper)));
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
        return getSingleResult(query(sql, null, new RowMapperResultSetExtractor<>(rowMapper)));
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object ...args) {
        return getSingleResult(query(sql, args, new RowMapperResultSetExtractor<>(rowMapper)));
    }

    private <T> T getSingleResult(List<T> results) {
        if (CollectionUtils.isEmpty(results)) {
            throw new SqlRunTimeException("Empty Result");
        }

        return results.stream().findFirst().get();
    }

    private <T> T execute(StatementCallback<T> qsc) throws SqlRunTimeException {
        if (Objects.isNull(qsc)) {
            throw new IllegalArgumentException();
        }

        try(Connection con = this.dataSource.getConnection();
            PreparedStatement ps = con.prepareCall(qsc.getSql())) {
            T result = qsc.executeStatement(ps);
            return result;
        }
        catch (SQLException e) {
            log.error("code: {}, messgea: {}", e.getErrorCode(), e.getMessage());
            throw new SqlRunTimeException();
        }
    }
}

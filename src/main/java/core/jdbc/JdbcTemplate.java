package core.jdbc;

import org.springframework.jdbc.core.PreparedStatementCallback;

import java.sql.*;
import java.util.List;

public class JdbcTemplate {

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... values) {
        return queryForObject(sql, rowMapper, newArgumentPreparedStatementSetter(values));
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        List<T> results = query(sql, rowMapper, pss);
        int size = results.size();
        if (size != 1) {
            throw new IncorrectResultSizeDataAccessException(size);
        }
        return results.iterator().next();
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        return query(sql, pss, new RowMapperResultSetExtractor<>(rowMapper));
    }

    private <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) {
        return query(new SimplePreparedStatementCreator(sql), pss, rse);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        return query(sql, new RowMapperResultSetExtractor<>(rowMapper));
    }

    private <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException {
        return execute(new QueryStatementCallback<>(sql, rse));
    }

    private <T> T query(PreparedStatementCreator psc, PreparedStatementSetter pss, ResultSetExtractor<T> rse) {
        return execute(psc, ps -> {
            if (pss != null) {
                pss.setValues(ps);
            }

            try (ResultSet rs = ps.executeQuery()) {
                return rse.extractData(rs);
            }
        });
    }

    public int update(String sql, Object... values) throws DataAccessException {
        return update(sql, newArgumentPreparedStatementSetter(values));
    }

    public int update(String sql, PreparedStatementSetter pss) throws DataAccessException {
        return update(new SimplePreparedStatementCreator(sql), pss);
    }

    private int update(PreparedStatementCreator psc, PreparedStatementSetter pss) throws DataAccessException {
        return execute(psc, ps -> {
            if (pss != null) {
                pss.setValues(ps);
            }
            return ps.executeUpdate();
        });
    }

    private <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws DataAccessException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = psc.createPreparedStatement(con)) {
            return action.doInPreparedStatement(ps);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private <T> T execute(StatementCallback<T> action) {
        try (Connection con = ConnectionManager.getConnection();
             Statement stmt = con.createStatement()) {
            return action.doInStatement(stmt);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private ArgumentPreparedStatementSetter newArgumentPreparedStatementSetter(Object[] values) {
        return new ArgumentPreparedStatementSetter(values);
    }
}

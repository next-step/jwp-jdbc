package core.jdbc;

import core.jdbc.exceptions.UnableToAccessException;

import java.sql.Connection;
import java.util.List;

public class CommonJdbc implements JdbcOperation {

    public CommonJdbc(Connection connection) {

    }

    @Override
    public <T> T queryForSingleObject(String sql, RowMapper<T> rowMapper, Object... args) throws UnableToAccessException {
        return null;
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws UnableToAccessException {
        return null;
    }

    @Override
    public int update(String sql, Object... args) throws UnableToAccessException {
        return 0;
    }
}

package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcRowMapper<T> implements RowMapper<T> {

    private Class<T> clazz;

    public JdbcRowMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T apply(ResultSet rs) throws SQLException {
        return new ResultSetSupport(rs).getResult(clazz);
    }
}

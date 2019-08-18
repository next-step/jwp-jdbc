package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SingleRowMapper<T> implements ResultMapper<ResultSet, T> {

    private final RowMapper<T> rowMapper;

    public SingleRowMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    public T resultMapping(ResultSet rs) throws SQLException {

        T value = null;

        while(rs.next()) {

            if(value != null) {
                throw new SQLException("has multipleRows!!!");
            }

            value = rowMapper.resultMapping(rs);
        }

        return value;
    }
}
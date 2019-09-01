package next.jdbc;

import next.jdbc.exception.SQLRuntimeException;

import java.sql.SQLException;

public class RowMapperWrapper {
    public static <T> RowMapper<T> wrapMapper(final ThrowableRowMapper<T> rowMapper) {
        return rs -> {
            try {
                return rowMapper.mapRow(rs);
            } catch (final SQLException ex) {
                throw new SQLRuntimeException(ex);
            }
        };
    }
}
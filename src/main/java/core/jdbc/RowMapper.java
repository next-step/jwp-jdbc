package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface to be implemented by objects that will map one object per row.
 *
 * @param <T> Type of object that will be mapped.
 * @author hyeyoom
 */
@FunctionalInterface
public interface RowMapper<T> {
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}

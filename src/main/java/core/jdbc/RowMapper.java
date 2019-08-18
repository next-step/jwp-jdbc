package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> extends ResultMapper<ResultSet, T> {
    T resultMapping(ResultSet rs) throws SQLException;
}
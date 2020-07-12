package core.jdbc.custom;

import java.sql.ResultSet;

public interface RowMapper<T> {

    T mapRowForObject(ResultSet resultSet) throws Exception;
}

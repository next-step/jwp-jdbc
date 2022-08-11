package core.jdbc;

import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper {
    User map(ResultSet rs) throws SQLException;
}

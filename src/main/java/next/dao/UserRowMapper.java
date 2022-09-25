package next.dao;

import core.jdbc.RowMapper;
import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapping(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getString("userId"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("email")
        );
    }
}

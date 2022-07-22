package next.dao;

import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    private static final UserRowMapper mapper = new UserRowMapper();

    private UserRowMapper() {
    }

    public static UserRowMapper getInstance() {
        return mapper;
    }

    @Override
    public User mapRow(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getString("userId"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("email"));
    }
}

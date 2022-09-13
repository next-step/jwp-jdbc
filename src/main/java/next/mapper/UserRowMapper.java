package next.mapper;

import core.jdbc.RowMapper;
import next.model.User;

import java.sql.ResultSet;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet) {
        try {
            return new User(resultSet.getString("userId"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("email"));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}

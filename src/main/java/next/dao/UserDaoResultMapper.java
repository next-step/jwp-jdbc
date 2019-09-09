package next.dao;

import core.jdbc.ResultMapper;
import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoResultMapper implements ResultMapper<User> {

    @Override
    public User mapResult(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
        );
    }
}

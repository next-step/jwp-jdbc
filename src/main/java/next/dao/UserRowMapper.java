package next.dao;

import core.jdbc.RowMapper;
import next.model.User;

import java.sql.ResultSet;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapping(ResultSet rs) throws Exception {
        return new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));
    }
}

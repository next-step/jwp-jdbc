package next.dao.mapper;

import core.jdbc.RowMapper;
import next.model.User;


import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapping(ResultSet rs) throws SQLException {
        User user = null;
        if(rs != null) {
            user = new User(rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"));
        }
        return user;
    }
}

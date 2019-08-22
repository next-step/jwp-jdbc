package next.dao.mapper;

import core.jdbc.ResultMapper;
import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hspark on 2019-08-22.
 */
public enum UserMapper implements ResultMapper<User> {
    INSTANCE;


    @Override
    public User map(ResultSet rs) throws SQLException {
        User user = null;
        if (rs.next()) {
            user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                    rs.getString("email"));
        }
        return user;
    }
}

package next.dao.mapper;

import core.jdbc.ResultMapper;
import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hspark on 2019-08-22.
 */
public enum UserListMapper implements ResultMapper<List<User>> {
    INSTANCE;

    @Override
    public List<User> map(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"));
            users.add(user);
        }
        return users;
    }
}

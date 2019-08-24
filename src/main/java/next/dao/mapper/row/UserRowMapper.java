package next.dao.mapper.row;

import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapResult(ResultSet rs) throws SQLException {
        User user = null;
        while (rs.next()) {
            user = new User(rs.getString("userId")
                    , rs.getString("password")
                    , rs.getString("name")
                    , rs.getString("email"));
        }

        return user;
    }
}

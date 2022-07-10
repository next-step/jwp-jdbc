package core.jdbc;

import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs) throws SQLException {
        if (Objects.nonNull(rs)) {
            return new User(rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"));

        }

        return null;
    }
}

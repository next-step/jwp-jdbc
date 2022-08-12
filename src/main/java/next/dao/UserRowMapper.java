package next.dao;

import core.jdbc.RowMapper;
import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    public static final String USERID = "userId";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String EMAIL = "email";

    @Override
    public User mapping(ResultSet rs) throws SQLException {
        return new User(rs.getString(USERID),
                rs.getString(PASSWORD),
                rs.getString(NAME),
                rs.getString(EMAIL));
    }
}

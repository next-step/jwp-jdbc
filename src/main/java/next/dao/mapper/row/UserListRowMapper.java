package next.dao.mapper.row;

import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserListRowMapper implements RowMapper<List<User>> {
    @Override
    public List<User> mapResult(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(new User(rs.getString("userId")
                    , rs.getString("password")
                    , rs.getString("name")
                    , rs.getString("email")));
        }

        return users;
    }
}

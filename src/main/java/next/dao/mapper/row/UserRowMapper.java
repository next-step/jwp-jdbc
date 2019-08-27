package next.dao.mapper.row;

import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRowMapper extends BasicRowMapper<User> implements RowMapper<User> {

    public UserRowMapper(String... targetFields) {
        super(User.class, targetFields);
    }

    @Override
    public List<User> mapResult(ResultSet rs) {
        List<User> users = new ArrayList<>();
        return super.mapObject(rs, users, User.class);
    }
}

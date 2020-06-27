package next.dao;

import core.jdbc.CommonJdbc;
import core.jdbc.ConnectionManager;
import core.jdbc.JdbcOperation;
import core.jdbc.RowMapper;
import next.model.User;

import java.util.List;

public class UserDao {

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getString("userId"), rs.getString("password"),
            rs.getString("name"), rs.getString("email"));

    public void insert(User user) {
        final JdbcOperation commonJdbc = new CommonJdbc(ConnectionManager.getDataSource());
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        commonJdbc.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        final JdbcOperation commonJdbc = new CommonJdbc(ConnectionManager.getDataSource());
        final String sql = "UPDATE users SET password=?, name=?, email=? WHERE userId=?";
        commonJdbc.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        final JdbcOperation commonJdbc = new CommonJdbc(ConnectionManager.getDataSource());
        final String sql = "SELECT userId, password, name, email FROM users";
        return commonJdbc.query(sql, userRowMapper, (Object[]) null);
    }

    public User findByUserId(String userId) {
        final JdbcOperation commonJdbc = new CommonJdbc(ConnectionManager.getDataSource());
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return commonJdbc.queryForSingleObject(sql, userRowMapper, userId);
    }
}

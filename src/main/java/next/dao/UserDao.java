package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private final JdbcTemplate template = JdbcTemplate.getInstance();

    public void insert(User user) {
        String sql = "insert into users values (?, ?, ?, ?)";
        template.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "update users " +
                "set password = ?, name = ?, email = ? " +
                "where userId = ?";

        template.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "select userId, password, name, email " +
                "from users";

        return (List<User>) template.query(sql, rs -> new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
        ));
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "select userId, password, name, email " +
                "from users " +
                "where userId = ?";

        return template.queryForObject(sql, rs -> new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")), userId);
    }
}

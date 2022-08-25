package next.dao;

import java.util.List;

import core.jdbc.support.template.JdbcTemplate;
import core.jdbc.support.template.RowMapper;
import next.model.User;

public class UserDao {
    private JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.insert(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";

        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
        String sql = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.selectAll(sql, rowMapper);
    }

    public User findByUserId(String userId) {
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"),
                                     rs.getString("password"),
                                     rs.getString("name"),
                                     rs.getString("email"));
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        return jdbcTemplate.selectOne(sql, rowMapper, userId);
    }
}

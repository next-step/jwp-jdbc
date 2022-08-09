package next.dao;

import java.util.List;

import core.jdbc.JdbcTemplate;
import core.jdbc.UserRowMapper;
import next.model.User;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.execute(sql, ps -> {
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());
        });
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET userId = ?, password = ?, name = ?, email = ? WHERE userId = ?";
        jdbcTemplate.execute(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), userId);
    }
}

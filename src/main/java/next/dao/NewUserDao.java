package next.dao;

import core.jdbc.JdbcRowMapper;
import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.List;

public class NewUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public NewUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        jdbcTemplate.executeUpdate("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail());
    }

    public void delete(String userId) {
        jdbcTemplate.executeUpdate("DELETE FROM USERS WHERE userId = ?", userId);
    }

    public void update(User user) {
        jdbcTemplate.executeUpdate("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId());
    }

    public List<User> findAll() {
        return jdbcTemplate.execute("SELECT userId, password, name, email FROM USERS", new JdbcRowMapper<>(User.class));
    }

    public User findByUserId(String userId) {
        return jdbcTemplate.executeOne("SELECT userId, password, name, email FROM USERS WHERE userId = ?", new JdbcRowMapper<>(User.class), userId)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("[" + userId + "] user does not exist");
                });
    }
}

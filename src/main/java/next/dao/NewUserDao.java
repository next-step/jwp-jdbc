package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import next.model.User;

import java.util.List;

public class NewUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public NewUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> rsMapper = rs -> new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"));

    public void insert(User user) {
        jdbcTemplate.executeUpdate("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail());
    }

    public void delete(String userId) {
        jdbcTemplate.executeUpdate("DELETE FROM USERS WHERE userId = ?",
                userId);
    }

    public void update(User user) {
        jdbcTemplate.executeUpdate("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId());
    }

    public List<User> findAll() {
        return jdbcTemplate.execute("SELECT userId, password, name, email FROM USERS", User.class);
    }

    public List<User> findAllByRsMapper() {
        return jdbcTemplate.execute("SELECT userId, password, name, email FROM USERS", rsMapper);
    }

    public User findByUserId(String userId) {
        return jdbcTemplate.executeOne("SELECT userId, password, name, email FROM USERS WHERE userId = ?", User.class, userId)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("[" + userId + "] user does not exist");
                });
    }

    public User findByUserIdByRsMapper(String userId) {
        return jdbcTemplate.executeOne("SELECT userId, password, name, email FROM USERS WHERE userId = ?", rsMapper, userId)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("[" + userId + "] user does not exist");
                });
    }
}

package next.dao;

import java.util.List;

import next.model.User;

public class UserDao {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void insert(User user) {
        jdbcTemplate.executeUpdate(
            "INSERT INTO USERS VALUES (?, ?, ?, ?)",
            user.getUserId(),
            user.getPassword(),
            user.getName(),
            user.getEmail()
        );
    }

    public void update(User user) {
        jdbcTemplate.executeUpdate(
            "UPDATE USERS SET password = ?, name = ?, email = ? where userId = ?",
            user.getPassword(),
            user.getName(),
            user.getEmail(),
            user.getUserId()
        );
    }

    public List<User> findAll() {
        return jdbcTemplate.getAll("SELECT userId, password, name, email FROM USERS", User.class);
    }

    public User findByUserId(String userId) {
        var query = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.getOne(query, User.class, userId);
    }
}

package next.dao;

import core.containter.SingletonContainer;
import core.jdbc.AutoRowConverter;
import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.List;

public class UserDao {
    private static final JdbcTemplate jdbcTemplate = SingletonContainer.getInstance(JdbcTemplate.class);

    public void insert(User user) {
        jdbcTemplate.execute(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        );
    }

    public void update(User user) {
        jdbcTemplate.execute(
                "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId()
        );
    }

    public List<User> findAll() {
        return jdbcTemplate.findAll(
                "SELECT userId, password, name, email FROM USERS",
                resultSet -> new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                )
        );
    }

    public User findByUserId(String userId) {
        return jdbcTemplate.findOne(
                "SELECT userId, password, name, email FROM USERS WHERE userId = ?",
                new AutoRowConverter<>(User.class),
                userId
        );
    }
}

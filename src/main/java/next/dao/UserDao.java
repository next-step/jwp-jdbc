package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapperFunction;
import java.util.List;
import java.util.NoSuchElementException;
import next.model.User;

public class UserDao {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.execute(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        jdbcTemplate.execute(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcTemplate.queryForList(sql, userRowMapperFunction());
    }

    public User findByUserId(String userId) {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.queryForObject(sql, userRowMapperFunction(), userId)
            .orElseThrow(NoSuchElementException::new);
    }

    private RowMapperFunction<User> userRowMapperFunction() {
        return resultSet -> new User(
            resultSet.getString("userId"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("email")
        );
    }
}

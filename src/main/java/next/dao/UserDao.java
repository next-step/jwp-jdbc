package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import core.jdbc.UserRowMapper;
import next.model.User;

import java.util.List;

public class UserDao {

    private final RowMapper<?> rowMapper = new UserRowMapper();

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(rowMapper);
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET name=?, email=? WHERE userId=?";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(rowMapper);
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(rowMapper);
        return jdbcTemplate.queryForList(sql);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(rowMapper);
        return jdbcTemplate.queryForObject(sql, userId);
    }
}

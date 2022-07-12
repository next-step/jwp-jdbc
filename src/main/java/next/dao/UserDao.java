package next.dao;

import next.model.User;

import java.util.List;

public class UserDao {

    private final JdbcTemplate<User> jdbcTemplate = new JdbcTemplate<>();

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        List<JdbcParameter> parameters = List.of(
                new JdbcParameter(1, user.getUserId()),
                new JdbcParameter(2, user.getPassword()),
                new JdbcParameter(3, user.getName()),
                new JdbcParameter(4, user.getEmail())
        );
        jdbcTemplate.insert(sql, parameters);
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        List<JdbcParameter> parameters = List.of(
                new JdbcParameter(1, user.getPassword()),
                new JdbcParameter(2, user.getName()),
                new JdbcParameter(3, user.getEmail()),
                new JdbcParameter(4, user.getUserId())
        );

        jdbcTemplate.update(sql, parameters);
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.findAll(sql, User.class);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT * FROM USERS WHERE userid=?";

        return jdbcTemplate.findOne(sql,List.of(new JdbcParameter(1, userId)), User.class);
    }
}

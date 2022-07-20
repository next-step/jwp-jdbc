package next.dao;

import next.model.User;

import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, List.of(user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        jdbcTemplate.update(sql, List.of( user.getPassword(), user.getName(), user.getEmail(), user.getUserId()));
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.findAll(sql, resultSet ->
                new User(resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")));
    }

    public User findByUserId(String userId) {
        String sql = "SELECT * FROM USERS WHERE userid=?";

        return jdbcTemplate.findOne(sql,new PreparedStatementValues(userId), resultSet ->
                new User(resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")));
    }
}

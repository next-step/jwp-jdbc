package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.List;

public class UserDao {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        });
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        jdbcTemplate.update(sql, new Object[]{
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId()
        });
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.queryForObject(sql,
                new Object[]{userId},
                resultSet -> new User(resultSet.getString("userId"), resultSet.getString("password"),
                        resultSet.getString("name"), resultSet.getString("email"))
        );
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcTemplate.query(sql,
                resultSet -> new User(resultSet.getString("userId"), resultSet.getString("password"),
                        resultSet.getString("name"), resultSet.getString("email"))
        );
    }

}

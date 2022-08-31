package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.List;

public class UserDao {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void insert(User user) {
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)",
            user.getUserId(),
            user.getPassword(),
            user.getName(),
            user.getEmail()
        );
    }

    public void update(User user) {
        jdbcTemplate.update("UPDATE USERS SET password=?,name=?, email=? WHERE userId=?",
                preparedStatement -> {
                    preparedStatement.setString(1, user.getPassword());
                    preparedStatement.setString(2, user.getName());
                    preparedStatement.setString(3, user.getEmail());
                    preparedStatement.setString(4, user.getUserId());
        }
        );
    }

    public List<User> findAll() {
        return jdbcTemplate.queryForList(
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
        return jdbcTemplate.queryForObject(
                "SELECT userId, password, name, email FROM USERS WHERE userid=?",
                resultSet -> new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                ),
                userId
        );
    }
}

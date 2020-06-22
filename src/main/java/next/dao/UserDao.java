package next.dao;

import core.jdbc.JdbcApi;
import next.model.User;

import java.util.List;

public class UserDao {
    private final JdbcApi jdbcApi = new JdbcApi();

    public void insert(User user) {
        jdbcApi.execute(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        );
    }

    public void update(User user) {
        jdbcApi.execute(
                "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId()
        );
    }

    public List<User> findAll() {
        return jdbcApi.findAll(
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
        return jdbcApi.findOne(
                "SELECT userId, password, name, email FROM USERS WHERE userId=?",
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

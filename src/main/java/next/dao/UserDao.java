package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.PreparedStatement;
import java.util.List;

public class UserDao {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void insert(User user) {
        jdbcTemplate.update(con -> {
            String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
            return preparedStatement;
        });
    }

    public void update(User user) {
        jdbcTemplate.update(connection -> {
            String sql = "UPDATE USERS SET password=?," +
                    "name=?, email=? WHERE userId=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getUserId());
            return preparedStatement;
        });
    }

    public List<User> findAll() {
        return jdbcTemplate.queryForList(
                connection -> connection.prepareStatement("SELECT userId, password, name, email FROM USERS"),
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
                connection -> {
                    String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, userId);
                    return preparedStatement;
                },
                resultSet -> new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                )
        );
    }
}

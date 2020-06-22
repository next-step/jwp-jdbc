package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowConverter;
import next.model.User;

import java.util.List;

public class UserDao {
    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private static final RowConverter<User> resultSetToUserConverter =
            resultSet -> new User(
                    resultSet.getString("userId"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
            );

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
                resultSetToUserConverter
        );
    }

    public User findByUserId(String userId) {
        return jdbcTemplate.findOne(
                "SELECT userId, password, name, email FROM USERS WHERE userId = ?",
                resultSetToUserConverter,
                userId
        );
    }
}

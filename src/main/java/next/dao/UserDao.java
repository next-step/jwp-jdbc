package next.dao;

import core.db.JdbcTemplate;
import next.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDao {
    private static UserDao INSTANCE = new UserDao();
    private JdbcTemplate jdbcTemplate;

    private UserDao() {
        this.jdbcTemplate = JdbcTemplate.getInstance();
    }

    public static UserDao getInstance() {
       return UserDao.INSTANCE;
    }

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcTemplate.selectMany(sql, getUserRowMapper());
    }

    public Optional<User> findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.selectOne(sql, getUserRowMapper(), userId);
    }

    private RowMapper<User> getUserRowMapper() {
        return (resultSet, row) -> {
            String userId = resultSet.getString("userId");
            String password = resultSet.getString("password");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");

            return new User(userId, password, name, email);
        };
    }
}

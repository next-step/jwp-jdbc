package next.dao;

import core.db.AllArgumentRowMapper;
import core.db.JdbcTemplate;
import next.model.User;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDao {
    private static UserDao INSTANCE = new UserDao();
    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> rowMapper;

    @SuppressWarnings("unchecked")
    private UserDao() {
        this.jdbcTemplate = JdbcTemplate.getInstance();
        this.rowMapper = new AllArgumentRowMapper(User.class);
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
        PreparedStatementSetter preparedStatementSetter = (PreparedStatement preparedStatement) -> {
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getUserId());
        };

        jdbcTemplate.update(sql, preparedStatementSetter);
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcTemplate.selectMany(sql, rowMapper);
    }

    public Optional<User> findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.selectOne(sql, rowMapper, userId);
    }
}

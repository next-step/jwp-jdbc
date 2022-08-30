package next.dao;

import java.util.List;

import core.jdbc.ConnectionManager;
import core.jdbc.support.template.JdbcTemplate;
import core.jdbc.support.template.RowMapper;
import next.model.User;

public class UserDao {
    private JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance(ConnectionManager.getDataSource());

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.insert(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void insertWithInterface(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.insert(sql, pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        });
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";

        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public void updateWithInterface(User user) {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";

        jdbcTemplate.update(sql, pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        });
    }

    public List<User> findAll() {
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
        String sql = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.selectAll(sql, rowMapper);
    }

    public User findByUserId(String userId) {
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"),
                                     rs.getString("password"),
                                     rs.getString("name"),
                                     rs.getString("email"));
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        return jdbcTemplate.selectOne(sql, rowMapper, userId);
    }

    public User findByUserIdWithInterface(String userId) {
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"),
                                                   rs.getString("password"),
                                                   rs.getString("name"),
                                                   rs.getString("email"));
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        return jdbcTemplate.selectOne(sql, rowMapper, pstmt -> pstmt.setString(1, userId));
    }
}

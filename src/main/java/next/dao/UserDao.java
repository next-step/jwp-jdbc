package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.QueryCallback;
import next.model.User;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final QueryCallback<User> queryCallback = new QueryCallback<User>() {
        @Override
        public User mapping(ResultSet rs) throws SQLException {
            User user = null;
            if(rs != null) {
                user = new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));
            }
            return user;
        }
    };

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        this.jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password = ?, name = ? , email = ? WHERE userId = ?";
        this.jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return this.jdbcTemplate.queryForList(sql, queryCallback);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return this.jdbcTemplate.queryForObject(sql, queryCallback, userId);
    }
}

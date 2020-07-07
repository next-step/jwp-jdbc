package next.dao;

import core.jdbc.JdbcTemplate;
import java.sql.SQLException;
import java.util.List;
import next.model.User;

public class UserDao {

    public void insert(User user) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String insertSql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertSql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String updateSql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        jdbcTemplate.update(updateSql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcTemplate.queryForList(sql,
                                         rs -> new User(rs.getString("userId"),
                                                        rs.getString("password"),
                                                        rs.getString("name"),
                                                        rs.getString("email")));
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        return jdbcTemplate.queryForObject(sql,
                                           rs -> new User(rs.getString("userId"),
                                                          rs.getString("password"),
                                                          rs.getString("name"),
                                                          rs.getString("email")),
                                           userId);
    }
}

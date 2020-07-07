package next.dao;

import core.jdbc.JdbcTemplate;
import java.util.List;
import next.model.User;

public class UserDao {

    public void insert(User user) {
        String insertSql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate.update(insertSql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String updateSql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        JdbcTemplate.update(updateSql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return JdbcTemplate.queryForList(sql,
                                         rs -> new User(rs.getString("userId"),
                                                        rs.getString("password"),
                                                        rs.getString("name"),
                                                        rs.getString("email")));
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        return JdbcTemplate.queryForObject(sql,
                                           rs -> new User(rs.getString("userId"),
                                                          rs.getString("password"),
                                                          rs.getString("name"),
                                                          rs.getString("email")),
                                           userId);
    }
}

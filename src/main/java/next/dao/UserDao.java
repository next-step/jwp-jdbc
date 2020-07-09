package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user)  {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.insertOrUpdate(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        String sql = "UPDATE USERS SET password = ? , name = ?, email= ?  WHERE userId = ?";
        jdbcTemplate.insertOrUpdate(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List findAll() {
        List<User> users = new ArrayList<>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate<User>();
        return jdbcTemplate.findAll("SELECT userId, password, name, email FROM USERS", rs -> {
            if (rs.next()) {
                users.add(new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email")));
            }
            return users;
        });
    }

    public User findByUserId(String userId)  {
        JdbcTemplate jdbcTemplate = new JdbcTemplate<User>();
        return (User) jdbcTemplate.findById("SELECT userId, password, name, email FROM USERS WHERE userid=?",
                rs -> new User(rs.getString("userId"), rs.getString("password"),
                rs.getString("name"), rs.getString("email")), userId);
    }

}

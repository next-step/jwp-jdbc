package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import next.model.User;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UserDao {

    public void insert(User user) throws SQLException{
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate.insert(sql,
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail());
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET userId = ?, password = ?, name = ?, email = ? WHERE userId = ?";
        JdbcTemplate.update(sql,
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS";
        ResultSet rs = JdbcTemplate.select(sql, null);

        ArrayList<User> userArrayList = new ArrayList<>();
        User user = null;
        if (rs.next()) {
            user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                    rs.getString("email"));
            userArrayList.add(user);
        }
        return userArrayList;
    }

    public User findByUserId(String userId) throws SQLException{
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        ResultSet rs = JdbcTemplate.select(sql, userId);

        User user = null;
        if (rs.next()) {
            user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                    rs.getString("email"));
        }

        return user;
    }
}

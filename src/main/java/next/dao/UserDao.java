package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends BaseDao {
    public void insert(User user) {
        save("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        save("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?", user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {

        Object result = select("SELECT userId, password, name, email FROM USERS", (rs) -> {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")));
            }
            return users;
        });

        return (List<User>) result;
    }

    public User findByUserId(String userId) {
        Object result = select("SELECT userId, password, name, email FROM USERS WHERE userid=?", (rs) -> {
            User user = null;
            if (rs.next()) {
                user = new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));
            }
            return user;
        }, new Object[]{userId});

        return (User) result;
    }
}

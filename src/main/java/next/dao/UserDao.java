package next.dao;

import core.jdbc.BaseDao;
import next.model.User;

import java.util.List;

public class UserDao extends BaseDao {
    public void insert(User user) {
        save("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        save("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?", user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        return selectByList("SELECT userId, password, name, email FROM USERS", (rs) -> {
            return new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"));
        });
    }

    public User findByUserId(String userId) {
        return (User) selectByObject("SELECT userId, password, name, email FROM USERS WHERE userid=?", (rs) -> {
            return new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"));
        }, userId);

    }
}

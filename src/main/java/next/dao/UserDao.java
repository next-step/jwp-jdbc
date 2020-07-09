package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        List<Object> objects = new ArrayList<>();
        objects.add(user.getUserId());
        objects.add(user.getPassword());
        objects.add(user.getName());
        objects.add(user.getEmail());

        int result = JdbcTemplate.executeUpdate(sql, objects.toArray());

        logger.debug("insert - {}", result);
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET name=?, email=? WHERE userid=?";

        List<Object> objects = new ArrayList<>();
        objects.add(user.getName());
        objects.add(user.getEmail());
        objects.add(user.getUserId());

        int result = JdbcTemplate.executeUpdate(sql, objects.toArray());

        logger.debug("update - {}", result);
    }

    public List<User> findAll() throws SQLException {

        String sql = "SELECT userId, password, name, email FROM USERS";

        return JdbcTemplate.executeQuery(sql, null, (rs) -> {
            List<User> userList = new ArrayList<>();
            User user = null;
            while (rs.next()) {

                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
                userList.add(user);
            }
            return userList;
        });
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        List<Object> objects = new ArrayList<>();
        objects.add(userId);

        return JdbcTemplate.executeQuery(sql, objects.toArray(), (rs) -> {
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
            return user;
        });
    }



}

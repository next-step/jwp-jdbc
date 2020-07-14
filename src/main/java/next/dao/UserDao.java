package next.dao;

import core.jdbc.JdbcTemplate;
import next.exception.DataAccessException;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        int result = JdbcTemplate.executeUpdate(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());

        logger.debug("insert - {}", result);
    }

    public void update(User user) throws DataAccessException {
        String sql = "UPDATE USERS SET name=?, email=? WHERE userid=?";

        int result = JdbcTemplate.executeUpdate(sql, user.getName(), user.getEmail(), user.getUserId());

        logger.debug("update - {}", result);
    }

    public List<User> findAll() throws DataAccessException {

        String sql = "SELECT userId, password, name, email FROM USERS";

        return JdbcTemplate.executeQuery(sql, User.class);
    }

    public User findByUserId(String userId) throws DataAccessException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        List<Object> objects = new ArrayList<>();
        objects.add(userId);

        return JdbcTemplate.executeQuery(sql, objects, User.class).get(0);
    }

    public List<User> findAllWithResultSet() throws DataAccessException {

        String sql = "SELECT userId, password, name, email FROM USERS";

        return JdbcTemplate.executeQuery(sql, (rs) -> {
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

    public User findByUserIdWithResultSet(String userId) throws DataAccessException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        List<Object> objects = new ArrayList<>();
        objects.add(userId);

        return JdbcTemplate.executeQuery(sql, objects, (rs) -> {
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
            return user;
        });
    }
}

package next.dao;

import next.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    void insert(User user) throws SQLException;

    void update(User user) throws SQLException;

    List<User> findAll() throws SQLException;

    User findByUserId(String userId) throws SQLException;
}

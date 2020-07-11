package next.dao;

import core.jdbc.custom.DefaultUserRepository;
import next.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private DefaultUserRepository defaultUserRepository = new DefaultUserRepository();

    public void insert(User user) throws SQLException {
        defaultUserRepository.save(user);
    }

    public void update(User user) throws SQLException {
        defaultUserRepository.save(user);
    }

    public List<User> findAll() throws SQLException {
        return defaultUserRepository.findAll();
    }

    public User findByUserId(String userId) throws SQLException {
        return defaultUserRepository.findById(userId);
    }
}

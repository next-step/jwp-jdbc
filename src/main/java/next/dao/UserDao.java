package next.dao;

import core.jdbc.custom.DefaultUserRepository;
import next.model.User;

import java.util.List;

public class UserDao {
    public void insert(User user) {
        DefaultUserRepository defaultUserRepository = new DefaultUserRepository();
        defaultUserRepository.save(user);
    }

    public void update(User user) {
        DefaultUserRepository defaultUserRepository = new DefaultUserRepository();
        defaultUserRepository.save(user);
    }

    public List<User> findAll() {
        DefaultUserRepository defaultUserRepository = new DefaultUserRepository();
        return defaultUserRepository.findAll();
    }

    public User findByUserId(String userId) {
        DefaultUserRepository defaultUserRepository = new DefaultUserRepository();
        return defaultUserRepository.findById(userId);
    }
}

package next.dao;

import next.model.User;

import java.util.List;

/**
 * Created by hspark on 2019-08-25.
 */
public interface UserDao {
    void insert(User user);
    void update(User user);
    List<User> findAll();
    User findByUserId(String userId);
}

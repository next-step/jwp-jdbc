package core.jdbc.custom;

import next.model.User;

import java.util.List;
import java.util.Map;

public class DefaultUserRepository extends AbstractRepository<User, String> {
    public DefaultUserRepository() {
        super(new User());
    }

    @Override
    public User find(final String query, final Map<String, Object> map) {
        return super.find(query, map);
    }

    @Override
    public void save(final User user) {
        super.save(user);
    }

    @Override
    public User findById(final String s) {
        return super.findById(s);
    }

    @Override
    public List<User> findAll() {
        return super.findAll();
    }
}

package core.jdbc.custom;

import next.model.User;

import java.util.List;

public class DefaultUserRepository extends AbstractRepository<User, String> {
    public DefaultUserRepository() {
        super(new User());
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

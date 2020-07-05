package core.jdbc.custom;

import next.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultUserRepository extends AbstractRepository {

    public User findById(final String id) {
        return (User) findById(User.class, id);
    }

    public List<? super Object> findAll() {
        return findAll(User.class).stream()
                .map(object -> (User)object)
                .collect(Collectors.toList());
    }

    @Override
    public Object findById(final Class clazz, final String s) {
        return super.findById(clazz, s);
    }

    @Override
    public List<? super Object> findAll(final Class clazz) {
        return super.findAll(clazz);
    }
}

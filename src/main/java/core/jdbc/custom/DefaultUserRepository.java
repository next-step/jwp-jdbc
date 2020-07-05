package core.jdbc.custom;

public class DefaultUserRepository extends AbstractRepository {

    @Override
    public void save(final Object object) {
        super.save(object);
    }

    @Override
    public Object findById(final Class clazz, final String s) {
        return super.findById(clazz, s);
    }
}

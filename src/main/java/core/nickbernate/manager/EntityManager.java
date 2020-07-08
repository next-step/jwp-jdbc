package core.nickbernate.manager;

import next.model.User;

public interface EntityManager {

    void begin();

    <T> T persist(T entity);

    <T> User findById(Class<T> entityClass, Object id);

    void flush();

    void commit();

}

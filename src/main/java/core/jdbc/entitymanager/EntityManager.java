package core.jdbc.entitymanager;

import next.model.User;

public interface EntityManager {

    void begin();

    void persist(Object entity);

    <T> User findById(Class<T> entityClass, Object id);

    void flush();

    void commit();
}

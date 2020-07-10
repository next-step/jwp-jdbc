package core.nickbernate.manager;

public interface EntityManager {

    void begin();

    <T> T persist(T entity);

    <T> T findById(Class<T> entityClass, Object id);

    void flush();

    void commit();

}

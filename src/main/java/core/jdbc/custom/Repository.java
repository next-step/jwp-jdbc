package core.jdbc.custom;

public interface Repository<T, V> {
    void save(T t);

    T findById(Class clazz, V v);

    void update(T t);
}

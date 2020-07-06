package core.jdbc.custom;

import java.util.List;

public interface Repository<T, V> {
    void save(T t);

    T findById(Class clazz, V v);

    List<? extends T> findAll(Class clazz);
}

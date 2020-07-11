package core.jdbc.custom;

import java.util.List;

public interface Repository<T, V> {
    void save(T t);

    T findById(V v);

    List<T> findAll();
}

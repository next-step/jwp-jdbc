package core.jdbc.custom;

import java.util.List;
import java.util.Map;

public interface Repository<T, V> {
    void save(T t);

    T findById(V v);

    T find(String query, Map<String, Object> v);

    List<T> findAll();
}

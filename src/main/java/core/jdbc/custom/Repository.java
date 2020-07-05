package core.jdbc.custom;

public interface Repository<T, V> {
    void save(T t);
}

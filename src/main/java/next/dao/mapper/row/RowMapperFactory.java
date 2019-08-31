package next.dao.mapper.row;

public class RowMapperFactory {
    public static <T> RowMapper<T> newInstance(Class<T> clazz, String... columns) {
        return new BasicRowMapper<>(clazz, columns);
    }
}

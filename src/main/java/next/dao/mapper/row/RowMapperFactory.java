package next.dao.mapper.row;

public class RowMapperFactory {
    public static <T> RowMapper<T> newInstance(Class<T> clazz) {
        return new BasicRowMapper<>(clazz);
    }
}

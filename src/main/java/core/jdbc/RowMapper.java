package core.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;

public interface RowMapper<T> {
    T mapRow(ResultSet resultSet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
}

package core.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapperV1 {

    <T> T getResultFromRow(Class<?> resultClazz, ResultSet resultSet) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

}

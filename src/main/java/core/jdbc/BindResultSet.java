package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created By kjs4395 on 2020-07-06
 */
public interface BindResultSet<T>{
    T bindResultSet(ResultSet resultSet) throws SQLException;
}

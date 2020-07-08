package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by iltaek on 2020/07/06 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public interface RowMapper<T> {

    T mapRow(ResultSet rs) throws SQLException;
}

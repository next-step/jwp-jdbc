package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by iltaek on 2020/07/06 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public interface PreparedStatementSetter {

    void setValue(PreparedStatement pstmt) throws SQLException;
}

package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created By kjs4395 on 2020-07-06
 */
public interface BindPrepareStatement {
    void setPrepareStatement(PreparedStatement pstmt) throws SQLException;
}

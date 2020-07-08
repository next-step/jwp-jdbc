package next.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlExecuteUpdate {

    void setQueryObject(PreparedStatement pstmt) throws SQLException;
}

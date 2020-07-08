package next.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlExecute {

    void execute(PreparedStatement pstmt) throws SQLException;
}

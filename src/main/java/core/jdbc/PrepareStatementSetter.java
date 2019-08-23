package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PrepareStatementSetter {

    void setValues(PreparedStatement ps) throws SQLException;
}

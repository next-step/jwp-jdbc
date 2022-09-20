package core.jdbc;

import java.sql.PreparedStatement;

public interface PreparedStatementSetter {
    void setValues(PreparedStatement ps) throws Exception;
}

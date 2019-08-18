package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateQueryAction implements QueryAction<Integer> {
    @Override
    public Integer action(PreparedStatement ps) throws SQLException {
        return ps.executeUpdate();
    }
}
package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectQueryAction implements QueryAction<ResultSet> {
    @Override
    public ResultSet action(PreparedStatement ps) throws SQLException {
        return ps.executeQuery();
    }
}
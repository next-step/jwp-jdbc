package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SimplePreparedStatementSetter implements PreparedStatementSetter {

    private final Object[] args;

    public SimplePreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        for (int idx = 1; idx <= args.length; idx++) {
            ps.setObject(idx, args[idx-1]);
        }
    }
}

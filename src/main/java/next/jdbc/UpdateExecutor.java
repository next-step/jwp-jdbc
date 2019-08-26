package next.jdbc;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateExecutor {
    public int execute(final String sql, final PreparedStatementParameterSetter parameterSetter) {
        try (
                final Connection con = ConnectionManager.getConnection();
                final PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            parameterSetter.setParameters(pstmt);
            return pstmt.executeUpdate();
        } catch (final SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}

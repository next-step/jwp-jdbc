package core.jdbc.callback;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class UpdateStatementCallback extends AbstractStatementCallback<Integer> {
    public UpdateStatementCallback(String sql) {
        super(sql);
    }

    @Override
    public Integer executeStatement(Statement stmt) throws SQLException {
        int updatedCount = stmt.executeUpdate(sql);
        log.debug("SQL update count: {}", updatedCount);
        return updatedCount;
    }
}

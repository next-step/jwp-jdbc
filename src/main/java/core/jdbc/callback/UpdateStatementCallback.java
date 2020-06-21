package core.jdbc.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@Slf4j
public class UpdateStatementCallback extends AbstractStatementCallback<Integer> {
    public UpdateStatementCallback(String sql) {
        super(sql, null);
    }

    public UpdateStatementCallback(String sql, PreparedStatementSetter pss) {
        super(sql, pss);
    }

    @Override
    public Integer executeStatement(PreparedStatement ps) throws SQLException {
        if (Objects.nonNull(pss)) {
            pss.setValues(ps);
        }

        int updatedCount = ps.executeUpdate();
        log.debug("SQL update count: {}", updatedCount);
        return updatedCount;
    }
}

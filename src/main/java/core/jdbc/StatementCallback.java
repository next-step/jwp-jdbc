package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementCallback<R> {
    R perform(PreparedStatement ps) throws SQLException;
}
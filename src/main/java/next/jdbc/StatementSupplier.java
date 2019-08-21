package next.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementSupplier {

    void supply(final PreparedStatement preparedStatement) throws SQLException;
}

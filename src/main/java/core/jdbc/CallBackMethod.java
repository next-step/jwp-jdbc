package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface CallBackMethod {
    Object invoke(PreparedStatement preparedStatement) throws SQLException;
}

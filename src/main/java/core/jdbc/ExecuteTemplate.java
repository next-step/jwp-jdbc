package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class ExecuteTemplate {

    void execute(String sql) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            setValues(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcExecuteException(e);
        }
    }

    abstract void setValues(PreparedStatement ps) throws SQLException;

}

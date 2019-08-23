package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class ExecuteTemplate {

    void execute() {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(query())) {

            setValues(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcExecuteException(e);
        }
    }

    abstract String query();

    abstract void setValues(PreparedStatement ps) throws SQLException;

}

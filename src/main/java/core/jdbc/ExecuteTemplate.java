package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    <R> R execute(String sql, ResultSetExtractor resultSetExtractor) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            setValues(ps);

            ResultSet rs = ps.executeQuery();
            return getResultMapper(resultSetExtractor, rs);
        } catch (SQLException e) {
            throw new JdbcExecuteException(e);
        }
    }

    abstract void setValues(PreparedStatement ps) throws SQLException;

    abstract <R> R getResultMapper(ResultSetExtractor extractor, ResultSet rs) throws SQLException;

}

package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ExecuteTemplate {

    void execute(String sql, Object... parameters) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            setValues(ps, parameters);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcExecuteException(e);
        }
    }

    <R> R execute(String sql, ResultSetExtractor resultSetExtractor, Object... parameters) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            setValues(ps, parameters);

            ResultSet rs = ps.executeQuery();
            return getResultMapper(resultSetExtractor, rs);
        } catch (SQLException e) {
            throw new JdbcExecuteException(e);
        }
    }

    private void setValues(PreparedStatement ps, Object[] parameters) throws SQLException {
        int i = 1;
        for (Object obj : parameters) {
            ps.setString(i++, obj.toString());
        }
    }

    abstract <R> R getResultMapper(ResultSetExtractor extractor, ResultSet rs) throws SQLException;

}

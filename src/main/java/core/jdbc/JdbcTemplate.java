package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {

    public void executeUpdate(String sql, Object... parameter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = populatePrepareStatement(con.prepareStatement(sql), parameter)) {
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new JdbcException(ex);
        }
    }

    public <T> List<T> execute(String sql, RowMapper<T> rowMapper, Object... parameter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = populatePrepareStatement(con.prepareStatement(sql), parameter);
             ResultSet rs = pstmt.executeQuery()) {
            return new ResultSetSupport(rs).getResult(rowMapper);
        } catch (SQLException ex) {
            throw new JdbcException(ex);
        }
    }

    public <T> Optional<T> executeOne(String sql, RowMapper<T> rowMapper, Object... parameter) {
        List<T> results = execute(sql, rowMapper, parameter);
        if (results.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(results.get(0));
    }

    public <T> List<T> execute(String sql, Class<T> clazz, Object... parameter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = populatePrepareStatement(con.prepareStatement(sql), parameter);
             ResultSet rs = pstmt.executeQuery()) {
            return new ResultSetSupport(rs).getResult(clazz);
        } catch (SQLException ex) {
            throw new JdbcException(ex);
        }
    }

    public <T> Optional<T> executeOne(String sql, Class<T> clazz, Object... parameter) {
        List<T> results = execute(sql, clazz, parameter);
        if (results.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(results.get(0));
    }

    private PreparedStatement populatePrepareStatement(PreparedStatement pstmt, Object[] parameter) throws SQLException {
        for (int i = 1; i <= parameter.length; i++) {
            pstmt.setObject(i, parameter[i-1]);
        }

        return pstmt;
    }

}

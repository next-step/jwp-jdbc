package core.jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate implements JdbcOperations {

    @Override
    public void execute(String sql, Object... parameters) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            for (SqlParameters.SqlParameter parameter : SqlParameters.of(parameters).toList()) {
                ps.setString(parameter.getIndex(), parameter.getValue());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcExecuteException(e);
        }
    }

    @Override
    public <T> List<T> queryForList(String sql, ResultSetExtractor<T> resultSetExtractor) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(resultSetExtractor.extractData(rs));
            }
            return results;
        } catch (SQLException e) {
            throw new JdbcExecuteException(e);
        }
    }

    @Override
    public <T> Optional<T> queryForObject(String sql, ResultSetExtractor<T> resultSetExtractor, Object... parameters) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (SqlParameters.SqlParameter parameter : SqlParameters.of(parameters).toList()) {
                pstmt.setString(parameter.getIndex(), parameter.getValue());
            }
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.ofNullable(resultSetExtractor.extractData(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new JdbcExecuteException(e);
        }
    }
}
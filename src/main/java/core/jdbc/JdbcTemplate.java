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
        PreparedStatementCreator preparedStatementCreator = getPreparedStatementCreator(sql, parameters);

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = preparedStatementCreator.createPreparedStatement(connection)) {
            preparedStatement.executeUpdate();
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
        PreparedStatementCreator preparedStatementCreator = getPreparedStatementCreator(sql, parameters);
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = preparedStatementCreator.createPreparedStatement(con);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return Optional.ofNullable(resultSetExtractor.extractData(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new JdbcExecuteException(e);
        }
    }

    private PreparedStatementCreator getPreparedStatementCreator(String sql, Object[] parameters) {
        SqlParameters sqlParameters = SqlParameters.of(parameters);
        return new PrepareStatementCreatorFactory(sql, sqlParameters);
    }
}
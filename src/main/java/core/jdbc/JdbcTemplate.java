package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public void save(String sql, PreparedStatementSetter preparedStatementSetter) {
        execute(sql, (pstmt) -> pstmt.executeUpdate(), preparedStatementSetter);
    }

    public void save(String sql, Object... parameters) {
        save(sql, (pss) -> setValues(pss, parameters));
    }

    public <T> T queryByObject(String sql, RowMapper<T> mapper, PreparedStatementSetter preparedStatementSetter) {
        return (T) query(sql, mapper, preparedStatementSetter).stream()
                .findFirst()
                .orElse(null);
    }

    public <T> T queryByObject(String sql, RowMapper<T> mapper, Object... parameters) {
        return queryByObject(sql, mapper, (pss) -> setValues(pss, parameters));
    }

    public List query(String sql, RowMapper mapper, PreparedStatementSetter preparedStatementSetter) {
        return (List) execute(sql, (PreparedStatement pstmt) -> {
            try (ResultSet rs = pstmt.executeQuery()) {
                List results = new ArrayList();
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
                return results;
            }
        }, preparedStatementSetter);
    }

    public List query(String sql, RowMapper mapper, Object... parameters) {
        return query(sql, mapper, (pss) -> setValues(pss, parameters));
    }

    private Object execute(String sql, QueryExecutor queryExecutor, PreparedStatementSetter preparedStatementSetter) {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            preparedStatementSetter.values(pstmt);
            return queryExecutor.executor(pstmt);
        } catch (SQLException e) {
            throw new DataSQLException(e);
        }
    }

    private void setValues(PreparedStatement pstmt, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            pstmt.setString(i + 1, (String) parameters[i]);
        }
    }
}

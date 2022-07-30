package next.dao;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public void update(String sql, PreparedStatementSetter preparedStatementSetter) {
        execute(sql,
                pstmt -> {
                    preparedStatementSetter.set(pstmt);
                    pstmt.executeUpdate();
                    return null;
                });
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return executeQuery(sql,
                pstmt -> {
                    List<T> resultObjects = new ArrayList<>();
                    ResultSet resultSet = pstmt.executeQuery();
                    while (resultSet.next()) {
                        resultObjects.add(rowMapper.mapRow(resultSet));
                    }
                    return resultObjects;
                });
    }

    public <T> T queryForObject(String sql, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) throws SQLException {
        return executeQuery(sql,
                pstmt -> {
                    preparedStatementSetter.set(pstmt);
                    ResultSet resultSet = pstmt.executeQuery();
                    if (resultSet.next()) {
                        return rowMapper.mapRow(resultSet);
                    }

                    return null;
                });
    }

    private <T> T executeQuery(String sql, PreparedStatementExecutor<T> preparedStatementExecutor) {
        try (Connection con = ConnectionManager.getConnection()) {
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                return preparedStatementExecutor.execute(pstmt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void execute(String sql, PreparedStatementExecutor<T> preparedStatementExecutor) {
        try (Connection con = ConnectionManager.getConnection()) {
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                preparedStatementExecutor.execute(pstmt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

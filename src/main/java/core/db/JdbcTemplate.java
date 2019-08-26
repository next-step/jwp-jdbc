package core.db;

import core.jdbc.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    public void executeUpdate(String sql, Object... params) {
        executeUpdate(sql, pstmt -> {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        });
    }

    public void executeUpdate(String sql, PreparedStatementSetter psmtSetter) {
        try {
            try (Connection con = ConnectionManager.getConnection();
                 PreparedStatement pstmt = con.prepareStatement(sql)) {
                psmtSetter.setPreparedStatement(pstmt);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }

    public <T> T executeQueryForObject(String sql, RowMapper<T> rowMapper, Object... params) {
        return executeQueryForObject(sql, pstmt -> {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        }, rowMapper);
    }

    public <T> T executeQueryForObject(String sql, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) {
        List<T> result = executeQuery(sql, pstmtSetter, rowMapper);
        return result.isEmpty() ? null : result.get(0);
    }

    public <T> List<T> executeQuery(String sql, RowMapper rowMapper, Object... params) {
        return executeQuery(sql, pstmt -> {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        }, rowMapper);
    }

    public <T> List<T> executeQuery(String sql, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmtSetter.setPreparedStatement(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {

                List<T> result = new ArrayList<>();
                if (rs.next()) {
                    result.add(rowMapper.mappedRow(rs));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }
}

package core.jdbc;

import core.exception.ExceptionStatus;
import core.exception.JdbcException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JdbcTemplate implements AutoCloseable {
    private Connection con;

    public JdbcTemplate() {
        this.con = ConnectionManager.getConnection();
    }

    public void update(String sql, PreparedStatementSetter preparedStatementSetter) {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            preparedStatementSetter.setValue(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.UPDATE_fAIL, e);
        }
    }

    public void update(String sql, Object... args) {
        update(sql, createPreparedStatementSetter(args));
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            preparedStatementSetter.setValue(pstmt);
            List<T> resultList = getResultList(pstmt, rowMapper);
            if (resultList.isEmpty()) {
                return null;
            }
            return resultList.get(0);
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.UPDATE_fAIL, e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
        PreparedStatementSetter preparedStatementSetter = createPreparedStatementSetter(args);
        return queryForObject(sql, rowMapper, preparedStatementSetter);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            preparedStatementSetter.setValue(pstmt);
            return getResultList(pstmt, rowMapper);
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.UPDATE_fAIL, e);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
        PreparedStatementSetter preparedStatementSetter = createPreparedStatementSetter(args);
        return query(sql, rowMapper, preparedStatementSetter);
    }

    private <T> List<T> getResultList(PreparedStatement pstmt, RowMapper<T> rowMapper) throws SQLException {
        List<T> resultList = new ArrayList<>();
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                resultList.add(rowMapper.mapRow(rs));
            }
        }
        return resultList;
    }

    public void beginTransaction() {
        setAutoCommit(false);
    }

    public void commit() {
        transactionCommit();
        setAutoCommit(true);
    }

    public void rollback() {
        try {
            this.con.rollback();
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.TRANSACTION_ROLLBACK_FAIL, e);
        }
    }

    private void transactionCommit() {
        try {
            this.con.commit();
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.TRANSACTION_COMMIT_FAIL, e);
        }
    }

    private void setAutoCommit(boolean autoCommit) {
        try {
            this.con.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.JDBC_SET_AUTO_COMMIT_FAIL, e);
        }
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object... args) {
        return preparedStatement -> {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
        };
    }

    @Override
    public void close() throws Exception {
        if (Objects.nonNull(this.con)) {
            this.con.close();
        }
    }
}

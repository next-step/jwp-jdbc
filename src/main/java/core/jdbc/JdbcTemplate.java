package core.jdbc;

import core.exception.ExceptionStatus;
import core.exception.JdbcException;
import core.util.ObjectMapperUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcTemplate implements AutoCloseable {
    private Connection con;

    public JdbcTemplate() {
        this.con = ConnectionManager.getConnection();
    }

    public void update(String sql, List<Object> args) {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            setPreparedStatement(pstmt, args);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.UPDATE_fAIL, e);
        }
    }

    public <T> T queryForObject(String sql, List<Object> args, Class<T> type) {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            setPreparedStatement(pstmt, args);
            return getObject(pstmt, type).orElse(null);
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.QUERY_FOR_OBJECT_fAIL, e);
        }
    }

    private <T> Optional<T> getObject(PreparedStatement pstmt, Class<T> type) {
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                Map<String, Object> resultMap = getFieldMap(rs, type);
                return Optional.of(ObjectMapperUtils.convertValue(resultMap, type));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.GET_OBJECT_FAIL);
        }
    }

    public <T> List<T> queryForList(String sql, List<Object> args, Class<T> type) {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            setPreparedStatement(pstmt, args);
            return getObjectList(pstmt, type);
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.GET_OBJECT_LIST_FAIL, e);
        }
    }

    private <T> List<T> getObjectList(PreparedStatement pstmt, Class<T> type) {
        List<T> resultList = new ArrayList<>();
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> resultMap = getFieldMap(rs, type);
                resultList.add(ObjectMapperUtils.convertValue(resultMap, type));
            }
            return resultList;
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.GET_OBJECT_FAIL);
        }
    }

    public <T> List<T> queryForList(String sql, Class<T> type) {
        List<T> resultList = new ArrayList<>();
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> resultMap = getFieldMap(rs, type);
                    resultList.add(ObjectMapperUtils.convertValue(resultMap, type));
                }
            }

            return resultList;
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.QUERY_FOR_LIST_fAIL, e);
        }
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

    private void setPreparedStatement(PreparedStatement pstmt, List<Object> args) {
        try {
            for (int i = 0; i < args.size(); i++) {
                pstmt.setObject(i + 1, args.get(i));
            }
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.NOT_CORRESPOND_PARAMETER_INDEX, e);
        }
    }

    private <T> Map<String, Object> getFieldMap(ResultSet rs, Class<T> type) {
        try {
            Map<String, Object> resultMap = new HashMap<>();
            for (Field field : type.getDeclaredFields()) {
                resultMap.put(field.getName(), rs.getObject(field.getName()));
            }
            return resultMap;
        } catch (SQLException e) {
            throw new JdbcException(ExceptionStatus.INVALID_COLUMN_LABEL, e);
        }
    }

    @Override
    public void close() throws Exception {
        if (Objects.nonNull(this.con)) {
            this.con.close();
        }
    }
}

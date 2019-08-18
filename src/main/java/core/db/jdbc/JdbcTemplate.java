package core.db.jdbc;

import com.google.common.collect.Lists;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author : yusik
 * @date : 2019-08-17
 */
public class JdbcTemplate {

    public int update(String sql, Object... args) {
        QueryExecutor<?, Integer> executor = (pstmt, mapper) -> pstmt.executeUpdate();
        return executor.execute(sql, null, args);
    }

    public int update(String sql, PreparedStatementSetter setter) {
        QueryExecutor<?, Integer> executor = (pstmt, mapper) -> {
            setter.setValues(pstmt);
            return pstmt.executeUpdate();
        };

        return executor.execute(sql, null);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
        QueryExecutor<T, List<T>> executor = (pstmt, mapper) -> getItemsByMapper(mapper, pstmt);
        return executor.execute(sql, rowMapper, args);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter setter) {
        QueryExecutor<T, List<T>> executor = (pstmt, mapper) -> {
            setter.setValues(pstmt);
            return getItemsByMapper(mapper, pstmt);
        };
        return executor.execute(sql, rowMapper);
    }

    public <T> List<T> query(String sql, Class<T> resultType, Object... args) {
        QueryExecutor<T, List<T>> executor = (pstmt, cb) -> getItemsByType(resultType, pstmt);
        return executor.execute(sql, null, args);
    }

    public <T> List<T> query(String sql, Class<T> resultType, PreparedStatementSetter setter) {
        QueryExecutor<T, List<T>> executor = (pstmt, mapper) -> {
            setter.setValues(pstmt);
            return getItemsByType(resultType, pstmt);
        };
        return executor.execute(sql, null);
    }

    public <T> Optional<T> querySingle(String sql, RowMapper<T> rowMapper, Object... args) {
        return Optional.of(query(sql, rowMapper, args))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0));
    }

    public <T> Optional<T> querySingle(String sql, Class<T> resultType, Object... args) {
        return Optional.of(query(sql, resultType, args))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0));
    }

    public <T> Optional<T> querySingle(String sql, RowMapper<T> rowMapper, PreparedStatementSetter setter) {
        return Optional.of(query(sql, rowMapper, setter))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0));
    }

    public <T> Optional<T> querySingle(String sql, Class<T> resultType, PreparedStatementSetter setter) {
        return Optional.of(query(sql, resultType, setter))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0));
    }

    private <T> List<T> getItemsByMapper(RowMapper<T> rowMapper, PreparedStatement pstmt) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            List<T> items = Lists.newArrayList();
            if (rs.next()) {
                T item = rowMapper.mapRow(rs);
                items.add(item);
            }
            return items;
        }
    }

    private <T> List<T> getItemsByType(Class<T> resultType, PreparedStatement pstmt) throws SQLException {
        List<T> items = Lists.newArrayList();
        try (ResultSet rs = pstmt.executeQuery()) {
            addToList(resultType, items, rs);
        } catch (ReflectiveOperationException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        return items;
    }

    private <T> void addToList(Class<T> resultType, List<T> items, ResultSet rs)
            throws ReflectiveOperationException, SQLException {
        Field[] fields = resultType.getDeclaredFields();
        Constructor<T> constructors = resultType.getDeclaredConstructor();
        ReflectionUtils.makeAccessible(constructors);
        if (rs.next()) {
            T item = constructors.newInstance();
            setItem(rs, fields, item);
            items.add(item);
        }
    }

    private <T> void setItem(ResultSet rs, Field[] fields, T item) throws SQLException, ReflectiveOperationException {
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            Object value = rs.getObject(fieldName);

            if (value != null) {
                ReflectionUtils.makeAccessible(fields[i]);
                fields[i].set(item, value);
            }
        }
    }

}

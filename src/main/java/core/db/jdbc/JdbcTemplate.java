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
        QueryExecutor<?, Integer> executor = (pstmt, callback) -> pstmt.executeUpdate();
        return executor.execute(sql, null, args);
    }

    public <T> List<T> query(String sql, QueryResultCallback<T> callback, Object... args) {
        QueryExecutor<T, List<T>> executor = (pstmt, cb) -> getItemsByCallback(cb, pstmt);
        return executor.execute(sql, callback, args);
    }

    public <T> List<T> query(String sql, Class<T> resultType, Object... args) {
        QueryExecutor<T, List<T>> executor = (pstmt, cb) -> getItemsByType(resultType, pstmt);
        return executor.execute(sql, null, args);
    }

    public <T> Optional<T> querySingle(String sql, QueryResultCallback<T> callback, Object... args) {
        return Optional.of(query(sql, callback, args))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0));
    }

    public <T> Optional<T> querySingle(String sql, Class<T> resultType, Object... args) {
        return Optional.of(query(sql, resultType, args))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0));
    }

    private <T> List<T> getItemsByCallback(QueryResultCallback<T> callback, PreparedStatement pstmt) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            List<T> items = Lists.newArrayList();
            if (rs.next()) {
                T item = callback.apply(rs);
                items.add(item);
            }
            return items;
        }
    }

    private <T> List<T> getItemsByType(Class<T> resultType, PreparedStatement pstmt) throws SQLException {
        List<T> items = Lists.newArrayList();
        try (ResultSet rs = pstmt.executeQuery()) {
            itemMapping(resultType, items, rs);
        } catch (ReflectiveOperationException e) {
            throw new JdbcException(e.getMessage(), e);
        }
        return items;
    }

    private <T> void itemMapping(Class<T> resultType, List<T> items, ResultSet rs)
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

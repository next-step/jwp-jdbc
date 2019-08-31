package core.jdbc;

import next.exception.DataAccessException;
import next.exception.ExceptionWrapper;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

public class JdbcContext {

    private final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public void executeUpdate(final String sql, final PreparedStatementSetter pss) {
        try (final Connection con = ConnectionManager.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql)) {

            pss.setParameters(pstmt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void executeUpdate(final String sql, final Object... arguments) {
        final PreparedStatementSetter pss = pstmt -> setObjects(pstmt, arguments);
        executeUpdate(sql, pss);
    }

    public <T> Optional<T> executeForObject(final String sql, final Class<T> clazz, final PreparedStatementSetter pss) {
        final List<T> executeForList = executeForList(sql, clazz, pss);
        return getOptionalT(executeForList);
    }

    public <T> Optional<T> executeForObject(final String sql, final RowMapper<T> rowMapper, final PreparedStatementSetter pss) {
        final List<T> executeForList = executeForList(sql, rowMapper, pss);
        return getOptionalT(executeForList);
    }

    private <T> Optional<T> getOptionalT(List<T> executeForList) {
        if (CollectionUtils.isEmpty(executeForList)) {
            return Optional.empty();
        }

        return Optional.of(executeForList.get(0));
    }

    public <T> Optional<T> executeForObject(final String sql, final Class<T> clazz, final Object... arguments) {
        final PreparedStatementSetter pss = pstmt -> setObjects(pstmt, arguments);
        return executeForObject(sql, clazz, pss);
    }

    public <T> Optional<T> executeForObject(final String sql, final RowMapper<T> rowMapper, final Object... arguments) {
        final PreparedStatementSetter pss = pstmt -> setObjects(pstmt, arguments);
        return executeForObject(sql, rowMapper, pss);
    }

    public <T> List<T> executeForList(final String sql, final RowMapper<T> rowMapper, final PreparedStatementSetter pss) {
        try (final Connection con = ConnectionManager.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql)) {

            pss.setParameters(pstmt);

            final ResultSet rs = pstmt.executeQuery();

            final List<T> list = new ArrayList<>();
            while (rs.next()) {
                list.add(rowMapper.mapRow(rs));
            }
            return list;
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> executeForList(final String sql, final RowMapper<T> rowMapper, final Object... arguments) {
        final PreparedStatementSetter pss = pstmt -> setObjects(pstmt, arguments);
        return executeForList(sql, rowMapper, pss);
    }

    public <T> List<T> executeForList(final String sql, final Class<T> clazz, final Object... arguments) {
        final PreparedStatementSetter pss = pstmt -> setObjects(pstmt, arguments);
        return executeForList(sql, clazz, pss);
    }

    public <T> List<T> executeForList(final String sql, final Class<T> clazz, final PreparedStatementSetter pss) {
        try {
            final Constructor<?> constructor = getConstructor(clazz);

            final RowMapper<T> rowMapper = rs -> {
                final Object[] parameters = getParameters(rs, constructor);
                return clazz.cast(constructor.newInstance(parameters));
            };

            return executeForList(sql, rowMapper, pss);
        } catch (NoSuchMethodException e) {
            throw new DataAccessException(e);
        }
    }

    private <T> Constructor<?> getConstructor(final Class<T> clazz) throws NoSuchMethodException {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElse(clazz.getDeclaredConstructor());
    }

    private Object[] getParameters(ResultSet rs, final Constructor<?> constructor) throws NoSuchMethodException {
        return getParameterNames(constructor)
                .map(ExceptionWrapper.function(rs::getString))
                .toArray();
    }

    private Stream<String> getParameterNames(final Constructor<?> constructor) throws NoSuchMethodException {
        final String[] parameterNames = nameDiscoverer.getParameterNames(constructor);
        if (parameterNames == null) {
            throw new NoSuchMethodException("필드를 매핑할 생성자가 없습니다.");
        }

        return Arrays.stream(parameterNames);
    }

    private void setObjects(PreparedStatement pstmt, Object... arguments) throws SQLException {
        for (int i = 0; i < arguments.length; i++) {
            pstmt.setObject(i + 1, arguments[i]);
        }
    }
}

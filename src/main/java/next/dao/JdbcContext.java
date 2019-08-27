package next.dao;

import core.jdbc.ConnectionManager;
import next.exception.DataAccessException;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcContext {

    public void executeUpdate(final String sql, PreparedStatementSetter pss) {
        try (final Connection con = ConnectionManager.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql)) {

            pss.setParameters(pstmt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void executeUpdate(final String sql, Object... arguments) {
        final PreparedStatementSetter pss = pstmt -> setObjects(pstmt, arguments);
        executeUpdate(sql, pss);
    }

    public <T> Optional<T> executeForObject(final String sql, final RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        final List<T> executeForList = executeForList(sql, rowMapper, pss);

        if (CollectionUtils.isEmpty(executeForList)) {
            return Optional.empty();
        }

        return Optional.of(executeForList.get(0));
    }

    public <T> Optional<T> executeForObject(final String sql, final RowMapper<T> rowMapper, Object... arguments) {
        final PreparedStatementSetter pss = pstmt -> setObjects(pstmt, arguments);
        return executeForObject(sql, rowMapper, pss);
    }

    public <T> List<T> executeForList(final String sql, final RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        try (final Connection con = ConnectionManager.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql)) {

            pss.setParameters(pstmt);

            final ResultSet rs = pstmt.executeQuery();

            final List<T> users = new ArrayList<>();
            while (rs.next()) {
                users.add(rowMapper.mapRow(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> executeForList(final String sql, final RowMapper<T> rowMapper, Object... arguments) {
        final PreparedStatementSetter preparedStatementSetter = pstmt -> setObjects(pstmt, arguments);
        return executeForList(sql, rowMapper, preparedStatementSetter);
    }

    private void setObjects(PreparedStatement pstmt, Object... arguments) throws SQLException {
        for (int i = 0; i < arguments.length; i++) {
            pstmt.setObject(i + 1, arguments[i]);
        }
    }
}

package core.jdbc;

import org.springframework.dao.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultJdbcTemplate implements JdbcTemplate{

    @Override
    public void update(String sql, PreparedStatementSetter pss) throws DataAccessException {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            pss.setValues(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(String sql, Object... values) throws DataAccessException {
        PreparedStatementSetter pss = createPreparedStatementSetter(values);
        update(sql, pss);
    }

    @Override
    public PreparedStatementSetter createPreparedStatementSetter(Object... values) {
        return new DefaultPreparedStatementSetter(List.of(values));
    }

    @Override
    public <T> T createForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) throws DataAccessException {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            pss.setValues(ps);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rowMapper.mapping(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T createForObject(String sql, RowMapper<T> rowMapper, Object... values) throws DataAccessException {
        return createForObject(sql, rowMapper, createPreparedStatementSetter(values));
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) throws DataAccessException {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            pss.setValues(ps);
            ResultSet rs = ps.executeQuery();

            List<T> result = new ArrayList<>();

            while (rs.next()) {
                result.add(rowMapper.mapping(rs));
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... values) throws DataAccessException {
        return query(sql, rowMapper, createPreparedStatementSetter(values));
    }
}

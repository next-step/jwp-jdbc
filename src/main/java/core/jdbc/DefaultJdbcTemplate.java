package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DefaultJdbcTemplate implements JdbcTemplate{

    @Override
    public void update(String sql, PreparedStatementSetter pss) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            pss.setValues(ps);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(String sql, Object... values) {
        PreparedStatementSetter pss = createPreparedStatementSetter(values);
        update(sql, pss);
    }

    @Override
    public PreparedStatementSetter createPreparedStatementSetter(Object... values) {
        return new DefaultPreparedStatementSetter(List.of(values));
    }

    @Override
    public <T> T createForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            pss.setValues(ps);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rowMapper.mapping(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T createForObject(String sql, RowMapper<T> rowMapper, Object... values) {
        return createForObject(sql, rowMapper, createPreparedStatementSetter(values));
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            pss.setValues(ps);
            ResultSet rs = ps.executeQuery();

            List<T> result = new ArrayList<>();

            while (rs.next()) {
                result.add(rowMapper.mapping(rs));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... values) {
        return query(sql, rowMapper, createPreparedStatementSetter(values));
    }
}

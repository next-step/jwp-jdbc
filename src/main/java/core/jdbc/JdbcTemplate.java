package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public void update(String sql, Object... args) {
        final PreparedStatementSetter preparedStatementSetter = createPreparedStatementSetter(args);
        update(sql, preparedStatementSetter);
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object... args) {
        return ps -> {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
        };
    }

    public void update(String sql, PreparedStatementSetter preparedStatementSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            preparedStatementSetter.setValues(pstmt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
        final PreparedStatementSetter preparedStatementSetter = createPreparedStatementSetter(args);
        return query(sql, rowMapper, preparedStatementSetter);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            preparedStatementSetter.setValues(pstmt);

            List<T> result = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(rowMapper.mapRow(rs));
                }
            }

            return result;
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
        final PreparedStatementSetter preparedStatementSetter = createPreparedStatementSetter(args);
        return queryForObject(sql, rowMapper, preparedStatementSetter);
    }


    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            preparedStatementSetter.setValues(pstmt);

            T result = null;
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result = rowMapper.mapRow(rs);
                }
            }

            return result;
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }
}

package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public static JdbcTemplate getInstance() {
        return jdbcTemplate;
    }

    public void update(String sql, Object... parameters) {
        try (PreparedStatement pstmt = toPreparedStatementByObjects(sql, parameters)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... parameters) {
        try (PreparedStatement pstmt = toPreparedStatementByObjects(sql, parameters);
             ResultSet rs = pstmt.executeQuery()) {

            List<T> result = queryForList(sql, rowMapper, parameters);

            if (result.isEmpty()) {
                return null;
            }
            return result.get(0);

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... parameters) {
        try (PreparedStatement pstmt = toPreparedStatementByObjects(sql, parameters);
             ResultSet rs = pstmt.executeQuery()) {
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private PreparedStatement toPreparedStatementByObjects(String sql, Object... parameters) throws SQLException {
        PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql);

        for (int i = 0; i < parameters.length; i++) {
            pstmt.setObject(i + 1, parameters[i]);
        }
        return pstmt;
    }
}

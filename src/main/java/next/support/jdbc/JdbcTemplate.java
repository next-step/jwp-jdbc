package next.support.jdbc;

import core.jdbc.ConnectionManager;

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

    public void update(String sql, Object... queryParameters) {
        try (PreparedStatement pstmt = createPreParedStatement(sql, queryParameters)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... queryParameters) {
        try (PreparedStatement pstmt = createPreParedStatement(sql, queryParameters);
             ResultSet rs = pstmt.executeQuery()){

            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }

            throw new RuntimeException();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... queryParameters) {
        try (PreparedStatement pstmt = createPreParedStatement(sql, queryParameters);
             ResultSet rs = pstmt.executeQuery()){

            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PreparedStatement createPreParedStatement(String sql, Object... queryParameters) throws SQLException {
        PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql);
        for (int i = 0; i < queryParameters.length; i++) {
            pstmt.setObject(i + 1, queryParameters[i]);
        }
        return pstmt;
    }
}

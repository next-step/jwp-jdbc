package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public void update(String sql, Object... args) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setArgs(pstmt, args);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    private void setArgs(PreparedStatement pstmt, Object[] args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            final int index = i + 1;
            final Object value = args[i];

            if (value instanceof String) {
                pstmt.setString(index, (String) args[i]);
            }
            if (value instanceof Integer) {
                pstmt.setInt(index, (Integer) args[i]);
            }
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
        ) {

            setArgs(pstmt, args);

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
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            setArgs(pstmt, args);

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

package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final JdbcTemplate INSTANCE = new JdbcTemplate();

    private JdbcTemplate() {
    }

    public static JdbcTemplate getInstance() {
        return INSTANCE;
    }

    public void update(String sql, Object... args) {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            setValues(ps, args);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setValues(PreparedStatement ps, Object[] args) throws SQLException {
        for (int idx = 1; idx <= args.length; idx++) {
            ps.setObject(idx, args[idx-1]);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rm) {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            return mapToEntity(rs, rm);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rm, Object... args) {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {
            setValues(ps, args);
            ResultSet rs = ps.executeQuery();
            return mapToEntity(rs, rm).iterator().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> List<T> mapToEntity(ResultSet rs, RowMapper<T> rm) throws SQLException {
        List<T> results = new ArrayList<>();
        while (rs.next()) {
            results.add(rm.mapRow(rs));
        }
        return results;
    }
}

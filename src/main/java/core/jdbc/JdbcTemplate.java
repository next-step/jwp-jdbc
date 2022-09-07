package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcTemplate {
    private static final JdbcTemplate INSTANCE = new JdbcTemplate();

    private JdbcTemplate() {
    }

    public static JdbcTemplate getInstance() {
        return INSTANCE;
    }

    public void update(String sql, Object... args) {
        update(new SimplePreparedStatementCreator(sql), new SimplePreparedStatementSetter(args));
    }

    public void update(String sql, PreparedStatementSetter pss) {
        update(new SimplePreparedStatementCreator(sql), pss);
    }

    private void update(PreparedStatementCreator psc, PreparedStatementSetter pss) {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement ps = psc.createPreparedStatement(con)
        ) {
            pss.setValues(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rm) {
        return query(new SimplePreparedStatementCreator(sql), new RowMapperResultSetExtractor<>(rm));
    }

    public <T> List<T> query(String sql, RowMapper<T> rm, Object... args) {
        return query(new SimplePreparedStatementCreator(sql), new RowMapperResultSetExtractor<>(rm), new SimplePreparedStatementSetter(args));
    }

    public <T> List<T> query(String sql, RowMapper<T> rm, PreparedStatementSetter pss) {
        return query(new SimplePreparedStatementCreator(sql), new RowMapperResultSetExtractor<>(rm), pss);
    }

    private <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse) {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement ps = psc.createPreparedStatement(con)
        ) {
            ResultSet rs = ps.executeQuery();
            return rse.extractData(rs);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse, PreparedStatementSetter pss) {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement ps = psc.createPreparedStatement(con)
        ) {
            pss.setValues(ps);
            ResultSet rs = ps.executeQuery();
            return rse.extractData(rs);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rm, Object... args) {
        return queryForObject(new SimplePreparedStatementCreator(sql), rm, new SimplePreparedStatementSetter(args));
    }

    public <T> T queryForObject(String sql, RowMapper<T> rm, PreparedStatementSetter pss) {
        return queryForObject(new SimplePreparedStatementCreator(sql), rm, pss);
    }

    private <T> T queryForObject(PreparedStatementCreator psc, RowMapper<T> rm, PreparedStatementSetter pss) {
        return query(psc, new RowMapperResultSetExtractor<>(rm), pss).iterator().next();
    }
}

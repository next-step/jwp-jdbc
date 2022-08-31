package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    private JdbcTemplate() {}

    public static JdbcTemplate getInstance() {
        return jdbcTemplate;
    }

    public List<Long> update(
            String sql,
            Object... parameters
    ) {
        List<Long> generatedKeys = new ArrayList<>();
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            setParameters(pstmt, parameters);
            pstmt.executeUpdate();
            ResultSet resultSet = pstmt.getGeneratedKeys();
            while (resultSet.next()) {
                generatedKeys.add(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return generatedKeys;
    }

    public <T> T queryForObject(
            String sql,
            RowMapper<T> rowMapper,
            Object... parameters
    ) {
        return query(
                sql,
                rowMapper,
                parameters
        ).get(0);
    }

    public <T> List<T> query(
            String sql,
            RowMapper<T> rowMapper,
            Object... parameters
    ) {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            setParameters(pstmt, parameters);
            return mapResultSet(rowMapper, pstmt.executeQuery());
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private void setParameters(
            PreparedStatement pstm,
            Object... parameters
    ) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            pstm.setObject(i + 1, parameters[i]);
        }
    }

    private <T> List<T> mapResultSet(
            RowMapper<T> rowMapper,
            ResultSet resultSet
    ) throws SQLException {
        List<T> rows = new ArrayList<>();
        while (resultSet.next()) {
            rows.add(rowMapper.mapRow(resultSet));
        }
        return rows;
    }
}

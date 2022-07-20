package next.dao;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    private JdbcTemplate() {
    }

    public static JdbcTemplate getInstance() {
        return jdbcTemplate;
    }

    public void update(String sql, Object... objects) {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            initPrepareStatement(new PreparedStatementValues(objects).getValues(), preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T findOne(String sql, RowMapper<T> rowMapper, Object... objects) {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = initPrepareStatement(new PreparedStatementValues(objects).getValues(), preparedStatement).executeQuery()) {

            if (rs.getFetchSize() > 1) {
                throw new IllegalArgumentException("반환 값이 1개 이상입니다.");
            }

            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }
            throw new IllegalArgumentException("대상을 찾을수 없습니다.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> findAll(String sql, RowMapper<T> rowMapper, Object... objects) {
        List<T> results = new ArrayList<>();
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet rs = initPrepareStatement(new PreparedStatementValues(objects).getValues(), preparedStatement).executeQuery()) {

            while (rs.next()) {
                results.add(rowMapper.mapRow(rs));
            }

            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PreparedStatement initPrepareStatement(List<PreparedStatementValue> values, PreparedStatement preparedStatement) throws SQLException {
        for (PreparedStatementValue value : values) {
            preparedStatement.setObject(value.getIndex(), value.getValue());
        }
        return preparedStatement;
    }
}

package core.jdbc;

import core.jdbc.exception.JdbcRuntimeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {

    public void execute(String sql, Object... args) throws JdbcRuntimeException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);) {
            setArguments(pstmt, args);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    public <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws JdbcRuntimeException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);) {
            setArguments(pstmt, args);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(rowMapper.mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... args) throws JdbcRuntimeException {
        List<T> rows = new ArrayList<>();
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);) {
            setArguments(pstmt, args);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                rows.add(rowMapper.mapRow(rs));
            }
            return rows;
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    private void setArguments(PreparedStatement pstmt, Object[] args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            pstmt.setObject(i + 1, args[i]);
        }
    }

}

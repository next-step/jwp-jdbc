package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public void update(String sql, PreparedStatementSetter pss) throws SQLException {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pss.setValues(pstmt);
            pstmt.executeUpdate();
        }
    }

    public void update(String sql, Object... params) throws SQLException {
        update(sql, setValues(params));
    }

    public <T> T selectForObject(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws SQLException {
        List<T> result = selectForList(sql, pss, rowMapper);
        return result.stream().findFirst().get();
    }

    public <T> T selectForObject(String sql, RowMapper<T> rowMapper, Object... params) throws SQLException {
        return selectForObject(sql, setValues(params), rowMapper);
    }

    public <T> List<T> selectForList(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws SQLException {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pss.setValues(pstmt);
            return map(pstmt, rowMapper);
        }
    }

    private <T> List<T> map(PreparedStatement pstmt, RowMapper<T> rowMapper) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }
            return result;
        }
    }

    public <T> List<T> selectForList(String sql, RowMapper<T> rowMapper, Object... params) throws SQLException {
        return selectForList(sql, setValues(params), rowMapper);
    }

    private PreparedStatementSetter setValues(Object[] params) {
        return pstmt -> {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            };
    }
}

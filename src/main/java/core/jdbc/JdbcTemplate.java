package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public enum JdbcTemplate {
    INSTANCE;

    public void update(PreparedStatementCreator preparedStatementCreator) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = preparedStatementCreator.createPreparedStatement(con))
        {
            pstmt.executeUpdate();
        }
    }

    public <T> T queryForObject(PreparedStatementCreator psCreator, RowMapper<T> rowMapper) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = psCreator.createPreparedStatement(con);
            ResultSet rs = pstmt.executeQuery())
        {
            rs.next();
            return rowMapper.mapRow(rs, 0);
        }
    }

    public <T> List<T> query(PreparedStatementCreator psCreator, RowMapper<T> rowMapper) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = psCreator.createPreparedStatement(con);
            ResultSet rs = pstmt.executeQuery())
        {
            List<T> results = new ArrayList<>();
            int i = 0;
            while (rs.next()) {
                results.add(rowMapper.mapRow(rs, i++));
            }
            return results;
        }
    }

}

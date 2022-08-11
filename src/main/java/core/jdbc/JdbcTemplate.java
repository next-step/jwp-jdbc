package core.jdbc;

import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
    public User queryForObject(String sql, RowMapper rowMapper, PreparedStatementParameterSetter setter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.set(pstmt);

            ResultSet rs = pstmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = rowMapper.map(rs);
            }
            rs.close();
            return user;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}

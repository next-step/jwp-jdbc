package core.jdbc;

import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public List<User> queryForList(String sql, RowMapper rm) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = rm.map(rs);
                users.add(user);
            }
            rs.close();
            return users;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public User queryForObject(String sql, RowMapper mapper, PreparedStatementParameterSetter setter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.set(pstmt);
            ResultSet rs = pstmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = mapper.map(rs);
            }
            rs.close();
            return user;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}

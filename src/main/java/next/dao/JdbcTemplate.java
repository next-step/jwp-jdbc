package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private JdbcTemplate() {
    }

    public static JdbcTemplate getInstance() {
        return new JdbcTemplate();
    }


    public void update(String sql, Object... parameters) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt =  con.prepareStatement(sql)) {

            setValues(pstmt, parameters);

            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setValues(PreparedStatement pstmt, Object... parameters) throws SQLException {
       for (int i = 0; i < parameters.length; i++) {
           pstmt.setString(i + 1, parameters[i].toString());
       }
    }

    public User queryForObject(String sql, RowMapper<User> rowMapper, Object... parameters) {
        List<User> users = query(sql, rowMapper, parameters);
        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

    public List<User> query(String sql, RowMapper<User> rowMapper, Object... parameters) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            List<User> users = new ArrayList<>();
            setValues(pstmt, parameters);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(rowMapper.mapRow(rs));
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

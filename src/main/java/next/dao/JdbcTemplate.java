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

    private static class InnerInstance {
        private static final JdbcTemplate instance = new JdbcTemplate();
    }


    public static JdbcTemplate getInstance() {
        return InnerInstance.instance;
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
           pstmt.setObject(i + 1, parameters[i]);
       }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... parameters) {
        List<T> list = query(sql, rowMapper, parameters);
        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... parameters) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            List<T> list = new ArrayList<>();
            setValues(pstmt, parameters);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rowMapper.mapRow(rs));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

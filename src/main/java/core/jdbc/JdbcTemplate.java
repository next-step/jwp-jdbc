package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public <T> List<T> queryForList(String sql, RowMapper<T> mapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            List<T> list = new ArrayList<>();
            while (rs.next()) {
                T mappedData = mapper.map(rs);
                list.add(mappedData);
            }
            rs.close();
            return list;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> mapper, PreparedStatementParameterSetter setter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.set(pstmt);

            ResultSet rs = pstmt.executeQuery();

            T mappedData = null;
            if (rs.next()) {
                mappedData = mapper.map(rs);
            }
            rs.close();
            return mappedData;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}

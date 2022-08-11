package core.jdbc;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public <T> T queryForObject(String sql, RowMapper<T> mapper, PreparedStatementParameterSetter setter) {
        List<T> list = this.queryForList(sql, mapper, setter);
        return list.isEmpty() ? null : list.get(0);
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> mapper) {
        return this.queryForList(sql, mapper, null);
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> mapper, @Nullable PreparedStatementParameterSetter setter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            if (setter != null) {
                setter.set(pstmt);
            }

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

    public void update(String sql, PreparedStatementParameterSetter setter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.set(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}

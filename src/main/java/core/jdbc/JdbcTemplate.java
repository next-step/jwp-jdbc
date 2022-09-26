package core.jdbc;

import core.jdbc.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate<T> {
    public void update(String sql, PreparedStatementSetter setter) {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)) {
            setter.setValues(pstmt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("fail to update");
        }
    }

    public List<T> query(String sql, RowMapper<T> mapper) {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
            List<T> list = new ArrayList<>();
            try {
                while(rs.next()) {
                    list.add(mapper.mapRow(rs));
                }
            } catch (SQLException e) {
                throw new DataAccessException("fail to convert resultSet");
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("fail to query");
        }
    }

    public T queryForObject(String sql, RowMapper<T> mapper, PreparedStatementSetter setter) {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
            setter.setValues(pstmt);
            try {
                if (rs.next()) {
                    return mapper.mapRow(rs);
                }
            } catch (SQLException e) {
                throw new DataAccessException("fail to convert resultSet");
            }
        } catch (SQLException e) {
            throw new DataAccessException("fail to query for object");
        }
        return null;
    }
}

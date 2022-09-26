package next.dao;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate<T> {
    public void update(String sql, PreparedStatementSetter setter) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            setter.setValues(pstmt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("fail to update");
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    throw new RuntimeException("fail to close preparedstatement");
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException("fail to close connection");
                }
            }
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
                throw new RuntimeException("fail to convert resultSet to dao list");
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("fail to query");
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
                throw new RuntimeException("fail to convert resultset to dao");
            }
        } catch (SQLException e) {
            throw new RuntimeException("fail to query for object");
        }
        throw new RuntimeException("fail to execute select query");
    }
}

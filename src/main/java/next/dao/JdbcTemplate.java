package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

abstract public class JdbcTemplate {

    public void insertOrUpdate(PreparedStatementSetter preparedStatementSetter) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = ConnectionManager.getConnection();
            String sql = createQuery();

            pstmt = con.prepareStatement(sql);

            preparedStatementSetter.values(pstmt);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
    public User queryForObject(RowMapper rowMapper, PreparedStatementSetter preparedStatementSetter) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = createQuery();
            pstmt = con.prepareStatement(sql);

            preparedStatementSetter.values(pstmt);

            rs = pstmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = (User) rowMapper.mapRow(rs);
            }

            return user;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public List<User> query(RowMapper rowMapper) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();
        try {
            con = ConnectionManager.getConnection();
            String sql = createQuery();
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while(rs.next()) {
                User findUser = (User) rowMapper.mapRow(rs);
                userList.add(findUser);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return userList;
    }

    abstract public String createQuery();
}

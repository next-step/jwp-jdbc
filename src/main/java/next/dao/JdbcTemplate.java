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

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQuery());
             )
        {
            preparedStatementSetter.values(pstmt);
        }
    }

    public User queryForObject(RowMapper rowMapper, PreparedStatementSetter preparedStatementSetter) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQuery());
             )
        {
            preparedStatementSetter.values(pstmt);

            return convertToObj(rowMapper, pstmt);
        }
    }

    public User convertToObj(RowMapper rowMapper, PreparedStatement pstmt) throws SQLException {
        User user = null;
        try (ResultSet rs = pstmt.executeQuery())
        {
            if (rs.next()) {
                user = (User) rowMapper.mapRow(rs);
            }
        }
        return user;
    }

    public List<User> query(RowMapper rowMapper) throws SQLException {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQuery());)
        {
            return convertToList(rowMapper, pstmt);
        }
    }

    public List<User> convertToList(RowMapper rowMapper, PreparedStatement pstmt) throws SQLException {
        List<User> userList = new ArrayList<>();
        try (ResultSet rs = pstmt.executeQuery())
        {
            while (rs.next()) {
                User findUser = (User) rowMapper.mapRow(rs);
                userList.add(findUser);
            }
        }
        return userList;
    }

    abstract public String createQuery();
}

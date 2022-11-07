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

            ResultSet rs = pstmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = (User) rowMapper.mapRow(rs);
            }

            return user;
        }
    }

    public List<User> query(RowMapper rowMapper) throws SQLException {
        List<User> userList = new ArrayList<>();
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQuery());
        )
        {
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                User findUser = (User) rowMapper.mapRow(rs);
                userList.add(findUser);
            }
        }

        return userList;
    }

    abstract public String createQuery();
}

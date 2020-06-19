package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    public void insert(User user) throws SQLException {
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", user);

        try(Connection con = ConnectionManager.getConnection()) {
            String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());

                pstmt.executeUpdate();
            }
        }
    }

    public void update(User user) throws SQLException {
        // TODO 구현 필요함.
    }

    public List<User> findAll() throws SQLException {
        // TODO 구현 필요함.
        return new ArrayList<User>();
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                User user = null;

                if (rs.next()) {
                    user = new User(rs.getString("userId"),
                                    rs.getString("password"),
                                    rs.getString("name"),
                                    rs.getString("email"));
                }

                return user;
            }
        }
    }
}

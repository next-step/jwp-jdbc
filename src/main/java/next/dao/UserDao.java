package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDao {
    private final QueryTemplate queryTemplate = new QueryTemplate();

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        Map<Integer, String> params = Map.of(
                1, user.getUserId(),
                2, user.getPassword(),
                3, user.getName(),
                4, user.getEmail()
        );
        queryTemplate.execute(sql, params);
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET name= ?, email= ? WHERE userId= ?";
        Map<Integer, String> params = Map.of(
                1, user.getName(),
                2, user.getEmail(),
                3, user.getUserId()
        );
        queryTemplate.execute(sql, params);
    }

    public List<User> findAll() throws SQLException {
        // TODO 구현 필요함.
        return new ArrayList<User>();
    }

    public User findByUserId(String userId) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
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
}

package next.dao;

import next.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) {
        JdbcTemplate insertJdbcTemplate = new JdbcTemplate();
        insertJdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", (pstmt) -> setValuesForInsert(user, pstmt));
    }

    void setValuesForInsert(User user, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, user.getUserId());
        pstmt.setString(2, user.getPassword());
        pstmt.setString(3, user.getName());
        pstmt.setString(4, user.getEmail());
    }

    public void update(User user) {
        JdbcTemplate updateJdbcTemplate = new JdbcTemplate();
        updateJdbcTemplate.update("UPDATE USERS SET name = ?, email = ? WHERE userId = ?", (pstmt) -> setValuesForUpdate(user, pstmt));
    }

    void setValuesForUpdate(User user, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, user.getName());
        pstmt.setString(2, user.getEmail());
        pstmt.setString(3, user.getUserId());
    }


    public List<User> findAll() {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate();
        return selectJdbcTemplate.query("SELECT userId, password, name, email FROM USERS", this::mapRowForFindAll);
    }

    private Object mapRowForFindAll(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                    rs.getString("email"));
            users.add(user);
        }
        return users;
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate();
        return (User) selectJdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?",
                pstmt -> setValuesForFindById(userId, pstmt), this::mapRowForFindById);
    }

    void setValuesForFindById(String userId, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, userId);
    }

    private Object mapRowForFindById(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                    rs.getString("email"));
        }
        return null;
    }
}

package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import java.sql.ResultSet;
import next.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private JdbcTemplate jdbcTemplate = JdbcTemplate.INSTANCE;
    private RowMapper<User> rowMapper = (rs, rowNum) -> new User(
        rs.getString("userId"),
        rs.getString("password"),
        rs.getString("name"),
        rs.getString("email")
    );

    private PreparedStatement setValue(PreparedStatement pstmt, Object... objects) throws SQLException {
        for (int i = 0; i < objects.length; ++i) {
            pstmt.setObject(i+1, objects[i]);
        }
        return pstmt;
    }

    public void insert(User user) throws SQLException {
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO USERS VALUES (?, ?, ?, ?)");
            return setValue(pstmt, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        });
    }

    public void update(User user) throws SQLException {
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?");
            setValue(pstmt, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
            return pstmt;
        });
    }

    public List<User> findAll() throws SQLException {
        return jdbcTemplate.query(
            con -> con.prepareStatement("SELECT * FROM USERS"),
            rowMapper
        );
    }

    public User findByUserId(String userId) throws SQLException {
        return jdbcTemplate.queryForObject(
            con -> {
                PreparedStatement pstmt = con.prepareStatement("SELECT userId, password, name, email FROM USERS WHERE userId=?");
                setValue(pstmt, userId);
                return pstmt;
            },
            rowMapper
        );
    }

}

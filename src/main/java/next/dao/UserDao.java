package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import next.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserDao {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    public void insert(User user) throws SQLException {
        int insertCount = jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        log.debug("insertCount: {}", insertCount);
    }

    public void update(User user) throws SQLException {
        int updateCount = jdbcTemplate.update(
            "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
            user.getPassword(),
            user.getName(),
            user.getEmail(),
            user.getUserId()
        );

        log.debug("updateCount: {}", updateCount);
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS";

        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return User.builder()
                .userId(rs.getString(1))
                .password(rs.getString(2))
                .name(rs.getString(3))
                .email(rs.getString(4))
                .build();
        });

        log.debug("user: {}", StringUtils.toPrettyJson(users));

        return users;
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid = ?";

        User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            return User.builder()
                .userId(rs.getString(1))
                .password(rs.getString(2))
                .name(rs.getString(3))
                .email(rs.getString(4))
                .build();
        }, userId);

        log.debug("user: {}", StringUtils.toPrettyJson(user));

        return user;
    }
}

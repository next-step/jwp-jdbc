package next.dao;

import next.dao.mapper.row.UserRowMapper;
import next.dao.mapper.sql.UserSqlMapper;
import next.dao.template.JdbcTemplate;
import next.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) throws SQLException {
        jdbcTemplate
                .executeUpdate(UserSqlMapper.insert()
                        , user.getUserId()
                        , user.getPassword()
                        , user.getName()
                        , user.getEmail());
    }

    public void update(User user) throws SQLException {
        jdbcTemplate
                .executeUpdate(UserSqlMapper.update()
                        , user.getName()
                        , user.getEmail()
                        , user.getUserId());
    }

    public User findByUserId(String userId) throws SQLException {
        UserRowMapper rowMapper = new UserRowMapper();
        return jdbcTemplate.selectOne(UserSqlMapper.select(), rowMapper, userId);
    }

    public List<User> findAll() throws SQLException {
        UserRowMapper rowMapper = new UserRowMapper();
        return jdbcTemplate.selectAll(UserSqlMapper.selectAll(), rowMapper);
    }
}

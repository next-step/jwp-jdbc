package next.dao;

import next.dao.mapper.row.RowMapper;
import next.dao.mapper.row.RowMapperFactory;
import next.dao.mapper.sql.UserSqlMapper;
import next.dao.template.JdbcTemplate;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.exception.DataAccessException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        try {
            jdbcTemplate
                    .executeUpdate(UserSqlMapper.insert()
                            , user.getUserId()
                            , user.getPassword()
                            , user.getName()
                            , user.getEmail());
        } catch (SQLException e) {
            DataAccessException.handleException(e);
        }
    }

    public void update(User user) {
        try {
            jdbcTemplate
                    .executeUpdate(UserSqlMapper.update()
                            , user.getName()
                            , user.getEmail()
                            , user.getUserId());
        } catch (SQLException e) {
            DataAccessException.handleException(e);
        }
    }

    public User findByUserId(String userId) {
        RowMapper<User> rowMapper = RowMapperFactory.newInstance(User.class);

        User user = null;
        try {
            user = jdbcTemplate.selectOne(UserSqlMapper.select(), rowMapper, userId);
        } catch (SQLException | IllegalAccessException e) {
            DataAccessException.handleException(e);
        }

        return user;
    }

    public List<User> findAll() {
        RowMapper<User> rowMapper = RowMapperFactory.newInstance(User.class);

        List<User> users = new ArrayList<>();
        try {
            users = jdbcTemplate.selectAll(UserSqlMapper.selectAll(), rowMapper);
        } catch (SQLException | IllegalAccessException e) {
            DataAccessException.handleException(e);
        }

        return users;
    }
}

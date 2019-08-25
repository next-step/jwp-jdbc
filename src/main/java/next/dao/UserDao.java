package next.dao;

import next.dao.mapper.row.UserListRowMapper;
import next.dao.mapper.row.UserRowMapper;
import next.dao.mapper.sql.UserSqlMapper;
import next.dao.template.JdbcTemplate;
import next.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        new JdbcTemplate()
                .executeUpdate(UserSqlMapper.insert()
                        , user.getUserId()
                        , user.getPassword()
                        , user.getName()
                        , user.getEmail());
    }

    public void update(User user) throws SQLException {
        new JdbcTemplate()
                .executeUpdate(UserSqlMapper.update()
                        , user.getName()
                        , user.getEmail()
                        , user.getUserId());
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate jdbc = new JdbcTemplate();

        UserRowMapper rowMapper = new UserRowMapper();
        return jdbc.selectOne(UserSqlMapper.select(), rowMapper, userId);
    }

    public List<User> findAll() throws SQLException {
        JdbcTemplate jdbc = new JdbcTemplate();

        UserListRowMapper rowMapper = new UserListRowMapper();
        return jdbc.selectAll(UserSqlMapper.selectAll(), rowMapper);
    }
}

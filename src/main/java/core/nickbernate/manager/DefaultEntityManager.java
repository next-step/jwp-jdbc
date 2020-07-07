package core.nickbernate.manager;

import core.jdbc.JdbcTemplate;
import core.nickbernate.exception.NickbernateExecuteException;
import core.nickbernate.session.Session;
import next.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DefaultEntityManager implements EntityManager {

    private Connection connection; // TODO: 2020/07/07 Connection 은 Session 이 가지고 있도록
    private JdbcTemplate jdbcTemplate;
    private Map<Object, Object> session2 = new HashMap<>(); // TODO: 2020/07/07 Map 을 가지고 있는 Session
    // TODO: 2020/07/07 엔티티를 key 로 entityCache 을 가지고 있는 Map
    // TODO: 2020/07/07 queryStore
    private Session session;

    public DefaultEntityManager(Connection connection, JdbcTemplate jdbcTemplate) {
        this.connection = connection;
        this.jdbcTemplate = jdbcTemplate;
    }

    public DefaultEntityManager(Session session) {
        this.session = session;
    }

    @Override
    public void begin() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new NickbernateExecuteException("Transaction beginning Failed.", e);
        }
    }

    @Override
    public <T> T persist(T entity) {
        User user = (User) entity;

        // TODO: 2020/07/07 queryStore 에 저장 필요
        this.session2.put(user.getUserId(), user);

        return entity;
    }

    @Override
    public <T> User findById(Class<T> entityClass, Object id) {
        if (session2.containsKey(id)) {
            return (User) session2.get(id);
        }

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.queryForObject(sql,
                new Object[]{id},
                resultSet -> new User(resultSet.getString("userId"), resultSet.getString("password"),
                        resultSet.getString("name"), resultSet.getString("email"))
        );
    }

    @Override
    public void flush() {
        User user = (User) session2.get("userId");
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        });
    }

    @Override
    public void commit() {
        flush();

        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new NickbernateExecuteException("Transaction commit Failed.", e);
        }
    }

}

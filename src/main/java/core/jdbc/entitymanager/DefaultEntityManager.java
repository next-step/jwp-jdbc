package core.jdbc.entitymanager;

import core.jdbc.JdbcTemplate;
import core.jdbc.entitymanager.exception.EntityManagerExecuteException;
import next.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DefaultEntityManager implements EntityManager {

    private Connection connection;
    private JdbcTemplate jdbcTemplate;
    private Map<Object, Object> session = new HashMap<>();

    public DefaultEntityManager(Connection connection, JdbcTemplate jdbcTemplate) {
        this.connection = connection;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void begin() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new EntityManagerExecuteException("Transaction beginning Failed.", e);
        }
    }

    @Override
    public void persist(Object entity) {
        User user = (User) entity;

        this.session.put(user.getUserId(), user);
    }

    @Override
    public <T> User findById(Class<T> entityClass, Object id) {
        if (session.containsKey(id)) {
            return (User) session.get(id);
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
        User user = (User) session.get("userId");
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
            throw new EntityManagerExecuteException("Transaction commit Failed.", e);
        }
    }

}

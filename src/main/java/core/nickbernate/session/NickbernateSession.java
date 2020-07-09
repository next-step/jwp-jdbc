package core.nickbernate.session;

import core.jdbc.JdbcTemplate;
import core.nickbernate.action.EntityActionQueue;
import core.nickbernate.persistence.PersistenceContext;
import core.nickbernate.persistence.StatefulPersistenceContext;
import core.nickbernate.util.EntityUtil;
import next.model.User;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class NickbernateSession implements Session {

    private DataSource dataSource;
    private EntityActionQueue actionQueue;
    private PersistenceContext persistenceContext;
    private JdbcTemplate jdbcTemplate;

    @Deprecated
    private Map<Object, Object> session2 = new HashMap<>();

    public NickbernateSession(DataSource dataSource) {
        this.dataSource = dataSource;
        this.actionQueue = new EntityActionQueue(this);
        this.persistenceContext = new StatefulPersistenceContext(this);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void begin() {
        jdbcTemplate.setConnectionAutoCommit(false);
    }

    @Override
    public <T> T persist(T entity) {
        EntityKey entityKey = EntityUtil.findEntityKeyFrom(entity);

        // TODO: 2020/07/07 actionQueue 에 저장 필요
        this.persistenceContext.addEntity(entityKey, entity);

        return entity;
    }

    @Override
    public <T> User findById(Class<T> entityClass, Object id) {
        // TODO: 2020/07/09 get from context
        if (session2.containsKey(id)) {
            return (User) session2.get(id);
        }

        // TODO: 2020/07/09 jdbcTemplate 이용한 entity reflection select method
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.queryForObject(sql,
                new Object[]{id},
                resultSet -> new User(resultSet.getString("userId"), resultSet.getString("password"),
                        resultSet.getString("name"), resultSet.getString("email"))
        );
    }

    @Override
    public void flush() {
        // TODO: 2020/07/09 1. context 에서 snapshot 비교과정을 통해 List<Action> 반환 (update)
        // TODO: 2020/07/09 2. queue에 전달
        // TODO: 2020/07/09 3. queue에서는 insert, update 문을 session -> jdbcTemplate 통해서 query

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

        jdbcTemplate.setConnectionAutoCommit(true);
    }

    @Override
    public void close() {
        jdbcTemplate.closeConnection();
    }
}

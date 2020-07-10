package core.nickbernate.session;

import core.jdbc.JdbcTemplate;
import core.nickbernate.action.EntityActionQueue;
import core.nickbernate.action.EntityInsertAction;
import core.nickbernate.action.EntitySelectAction;
import core.nickbernate.exception.NickbernateExecuteException;
import core.nickbernate.persistence.PersistenceContext;
import core.nickbernate.persistence.StatefulPersistenceContext;
import core.nickbernate.util.EntityUtil;
import next.model.User;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
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
        EntityKey entityKey = EntityUtil.createEntityKeyFrom(entity);

        EntityInsertAction entityInsertAction = new EntityInsertAction(entity);
        this.actionQueue.add(entityInsertAction);

        this.persistenceContext.addEntity(entityKey, entity);

        return entity;
    }

    @Override
    public <T> T findById(Class<T> entityClass, Object id) {
        EntityKey entityKey = new EntityKey(entityClass, id);
        if (persistenceContext.containsKey(entityKey)) {
            return (T) persistenceContext.getEntity(entityKey);
        }

        EntitySelectAction entitySelectAction = new EntitySelectAction(entityClass, id);
        T entity = executeSelectQuery(entityClass, entitySelectAction);
        this.persistenceContext.addEntity(entityKey, entity);

        return entity;
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

    public <T> T executeSelectQuery(Class<T> entityClass, EntitySelectAction entitySelectAction) {
        return jdbcTemplate.queryForObject(entitySelectAction.getQuery(), resultSet -> {
            T instance = EntityUtil.createNewInstance(entityClass);
            List<Field> fields = EntityUtil.scanEntityFields(entityClass);

            try {
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(instance, resultSet.getObject(field.getName()));
                }
            } catch (IllegalAccessException e) {
                throw new NickbernateExecuteException("Entity creation binding failed.", e);
            }

            return instance;
        });
    }

}

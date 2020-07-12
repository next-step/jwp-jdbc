package core.nickbernate.session;

import core.jdbc.JdbcTemplate;
import core.nickbernate.action.EntityAction;
import core.nickbernate.action.EntityActionQueue;
import core.nickbernate.action.EntityInsertAction;
import core.nickbernate.action.EntitySelectAction;
import core.nickbernate.exception.NickbernateExecuteException;
import core.nickbernate.persistence.EntityKey;
import core.nickbernate.persistence.PersistenceContext;
import core.nickbernate.persistence.StatefulPersistenceContext;
import core.nickbernate.util.EntityUtil;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.List;

public class NickbernateSession implements Session {

    private EntityActionQueue actionQueue;
    private PersistenceContext persistenceContext;
    private JdbcTemplate jdbcTemplate;

    public NickbernateSession(DataSource dataSource) {
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
        List<EntityAction> updateActions = this.persistenceContext.generateActionsWithdirtyChecking();
        actionQueue.addAll(updateActions);

        List<String> queries = actionQueue.getAllQueries();
        executeUpdateQuery(queries);
    }

    @Override
    public void commit() {
        flush();

        jdbcTemplate.setConnectionAutoCommit(true);

        close();
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

    private void executeUpdateQuery(List<String> queries) {
        jdbcTemplate.bulkUpdate(queries);
    }

}

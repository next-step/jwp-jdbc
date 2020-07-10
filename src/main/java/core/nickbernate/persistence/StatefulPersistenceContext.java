package core.nickbernate.persistence;

import core.nickbernate.exception.NickbernateExecuteException;
import core.nickbernate.session.EntityKey;
import core.nickbernate.session.Session;
import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.Map;

public class StatefulPersistenceContext implements PersistenceContext {

    private Session session;
    private Map<EntityKey, Object> entities;
    private Map<EntityKey, Object> entitySnapshots;

    public StatefulPersistenceContext(Session session) {
        this.session = session;
        this.entities = new HashMap<>();
        this.entitySnapshots = new HashMap<>();
    }

    @Override
    public void addEntity(EntityKey entityKey, Object entity) {
        this.entities.put(entityKey, entity);

        Object snapshot = copyFromEntity(entity);
        this.entitySnapshots.put(entityKey, snapshot);
    }

    @Override
    public boolean containsKey(EntityKey entityKey) {
        return entities.containsKey(entityKey);
    }

    @Override
    public Object getEntity(EntityKey entityKey) {
        return entities.get(entityKey);
    }

    private Object copyFromEntity(Object entity) {
        try {
            Object dest = entity.getClass().getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(dest, entity);

            return dest;
        } catch (ReflectiveOperationException e) {
            throw new NickbernateExecuteException("Entity snapshot creation failed.", e);
        }
    }

}

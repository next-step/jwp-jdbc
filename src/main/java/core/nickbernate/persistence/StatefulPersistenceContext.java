package core.nickbernate.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.nickbernate.action.EntityAction;
import core.nickbernate.action.EntityUpdateAction;
import core.nickbernate.exception.NickbernateExecuteException;
import core.nickbernate.session.EntityKey;
import core.nickbernate.session.Session;
import core.nickbernate.util.EntityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatefulPersistenceContext implements PersistenceContext {

    private Session session;
    private ObjectMapper objectMapper = new ObjectMapper();
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

    @Override
    public List<EntityAction> generateActionsWithdirtyChecking() {
        List<EntityAction> actions = new ArrayList<>();
        for (Map.Entry<EntityKey, Object> entry : entities.entrySet()) {
            EntityKey entityKey = entry.getKey();
            Object entity = entry.getValue();

            Object snapShot = entitySnapshots.get(entityKey);
            if (existsDirtyField(entity, snapShot)) {
                EntityUpdateAction updateAction = new EntityUpdateAction(entity);
                actions.add(updateAction);
            }
        }

        return actions;
    }

    private boolean existsDirtyField(Object entity, Object snapShot) {
        return !EntityUtil.isAllSameEntityFieldValues(entity, snapShot);
    }

    private Object copyFromEntity(Object entity) {
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(entity), entity.getClass());
        } catch (JsonProcessingException e) {
            throw new NickbernateExecuteException("Entity snapshot creation failed.", e);
        }
    }

}

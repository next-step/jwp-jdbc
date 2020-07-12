package core.nickbernate.persistence;

import core.nickbernate.action.EntityAction;
import core.nickbernate.session.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class StatefulPersistenceContext implements PersistenceContext {

    private Session session;
    private Map<EntityKey, EntitySnapshotModel> entitySnapshotModels;

    public StatefulPersistenceContext(Session session) {
        this.session = session;
        this.entitySnapshotModels = new HashMap<>();
    }

    @Override
    public void addEntity(EntityKey entityKey, Object entity) {
        EntitySnapshotModel entitySnapshotModel = new EntitySnapshotModel(entity);

        this.entitySnapshotModels.put(entityKey, entitySnapshotModel);
    }

    @Override
    public boolean containsKey(EntityKey entityKey) {
        return entitySnapshotModels.containsKey(entityKey);
    }

    @Override
    public Object getEntity(EntityKey entityKey) {
        EntitySnapshotModel entitySnapshotModel = entitySnapshotModels.get(entityKey);

        return entitySnapshotModel.getEntity();
    }

    @Override
    public List<EntityAction> generateActionsWithdirtyChecking() {
        return entitySnapshotModels.values().stream()
                .map(EntitySnapshotModel::checkDirtyFields)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

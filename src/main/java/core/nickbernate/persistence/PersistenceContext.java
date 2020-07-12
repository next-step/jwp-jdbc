package core.nickbernate.persistence;

import core.nickbernate.action.EntityAction;

import java.util.List;

public interface PersistenceContext {

    void addEntity(EntityKey entityKey, Object entity);

    boolean containsKey(EntityKey entityKey);

    Object getEntity(EntityKey entityKey);

    List<EntityAction> generateActionsWithdirtyChecking();

}

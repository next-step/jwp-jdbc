package core.nickbernate.persistence;

import core.nickbernate.session.EntityKey;

public interface PersistenceContext {

    void addEntity(EntityKey entityKey, Object entity);

    boolean containsKey(EntityKey entityKey);

    Object getEntity(EntityKey entityKey);
}

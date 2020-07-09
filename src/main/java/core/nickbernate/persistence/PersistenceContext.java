package core.nickbernate.persistence;

import core.nickbernate.session.EntityKey;

public interface PersistenceContext {

    void addEntity(EntityKey entityKey, Object entity);

}

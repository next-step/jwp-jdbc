package core.nickbernate.session;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class EntityKey {

    private Class<?> entityClass;
    private Object id;

    public EntityKey(Class<?> entityClass, Object id) {
        this.entityClass = entityClass;
        this.id = id;
    }
}

package core.nickbernate.action;

import core.nickbernate.util.EntityQueryCreationUtil;
import core.nickbernate.util.EntityUtil;

public class EntitySelectAction extends EntityAction {

    public <T> EntitySelectAction(Class<T> entityClass, Object id) {
        super(entityClass);
        super.query = createSelectQuery(entityClass, id);
    }

    private String createSelectQuery(Class<?> entityClass, Object id) {
        return createSelectQuery(entityClass) + createWhereIdQuery(entityClass, id);
    }

    private String createSelectQuery(Class<?> entityClass) {
        String tableName = getTableName(entityClass);
        String selectTargets = EntityQueryCreationUtil.createFieldTargetQuery(entityClass);

        return String.format("SELECT %s FROM %s", selectTargets, tableName);
    }

    private String createWhereIdQuery(Class<?> entityClass, Object id) {
        return String.format(" WHERE %s='%s'", EntityUtil.findEntityIdField(entityClass).getName(), id);
    }

}

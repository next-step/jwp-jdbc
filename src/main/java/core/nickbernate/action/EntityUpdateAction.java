package core.nickbernate.action;

import core.nickbernate.util.EntityQueryCreationUtil;
import core.nickbernate.util.EntityUtil;

import java.lang.reflect.Field;

public class EntityUpdateAction extends EntityAction {

    public <T> EntityUpdateAction(T entity) {
        super(entity.getClass());
        super.query = createUpdateQuery(entity);
    }

    private <T> String createUpdateQuery(T entity) {
        Class<?> entityClass = entity.getClass();

        String tableName = getTableName(entityClass);
        String updateFieldsQuery = EntityQueryCreationUtil.createUpdateFieldsQuery(entity);

        Field idField = EntityUtil.findEntityIdField(entityClass);
        Object idFieldValue = EntityUtil.getIdFieldValue(entity);

        return String.format("UPDATE %s SET %s WHERE %s='%s'", tableName, updateFieldsQuery, idField.getName(), idFieldValue);
    }

}

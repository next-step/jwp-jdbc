package core.nickbernate.action;

import core.nickbernate.util.EntityQueryCreationUtil;

public class EntityUpdateAction extends EntityAction {

    public <T> EntityUpdateAction(T entity) {
        super(entity.getClass());
        super.query = createUpdateQuery(entity);
    }

    private <T> String createUpdateQuery(T entity) {
        // TODO: 2020/07/10
        Class<?> entityClass = entity.getClass();

        String tableName = getTableName(entityClass);
        String insertFieldValuesQuery = EntityQueryCreationUtil.createFieldValuesQuery(entity);

        return String.format("INSERT INTO %s VALUES (%s)", tableName, insertFieldValuesQuery);
    }

}

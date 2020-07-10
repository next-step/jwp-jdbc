package core.nickbernate.action;

import core.nickbernate.util.EntityQueryCreationUtil;

public class EntityInsertAction extends EntityAction {

    public <T> EntityInsertAction(T entity) {
        super(entity.getClass());
        super.query = createInsertQuery(entity);
    }

    private <T> String createInsertQuery(T entity) {
        Class<?> entityClass = entity.getClass();

        String tableName = getTableName(entityClass);
        String insertFieldValuesQuery = EntityQueryCreationUtil.createFieldValuesQuery(entity);

        return String.format("INSERT INTO %s VALUES (%s)", tableName, insertFieldValuesQuery);
    }

}

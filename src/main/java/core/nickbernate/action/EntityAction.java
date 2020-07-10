package core.nickbernate.action;

import core.nickbernate.annotation.Entity;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public abstract class EntityAction {

    private Class<?> entityClass;
    protected String query;

    protected EntityAction(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    protected String getTableName(Class<?> entityClass) {
        Entity entityClassAnnotation = entityClass.getAnnotation(Entity.class);

        return StringUtils.isEmpty(entityClassAnnotation.table()) ? entityClass.getSimpleName() : entityClassAnnotation.table();
    }

}

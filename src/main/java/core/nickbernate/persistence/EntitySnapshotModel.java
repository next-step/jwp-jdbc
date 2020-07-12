package core.nickbernate.persistence;

import core.nickbernate.action.EntityUpdateAction;
import core.nickbernate.util.EntityUtil;
import lombok.Getter;

import javax.annotation.Nullable;

@Getter
public class EntitySnapshotModel {

    private Object entity;
    private Object snapshot;

    public EntitySnapshotModel(Object entity) {
        this.entity = entity;
        this.snapshot = EntityUtil.copyFromEntity(entity);
    }

    @Nullable
    public EntityUpdateAction checkDirtyFields() {
        if (existsDirtyField(entity, snapshot)) {
            return new EntityUpdateAction(entity);
        }

        return null;
    }

    private boolean existsDirtyField(Object entity, Object snapShot) {
        return !EntityUtil.isAllSameEntityFieldValues(entity, snapShot);
    }

}

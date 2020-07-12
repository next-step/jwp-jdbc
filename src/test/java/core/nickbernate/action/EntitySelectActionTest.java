package core.nickbernate.action;

import core.nickbernate.TestEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntitySelectActionTest {

    @DisplayName("Select Query 생성하기")
    @Test
    void createSelectQuery() {
        /* given */
        Class<TestEntity> entityClass = TestEntity.class;
        String entityId = "testId";

        /* when */
        EntitySelectAction selectAction = new EntitySelectAction(entityClass, entityId);

        /* then */
        assertThat(selectAction.getQuery()).isEqualTo(
                String.format("SELECT id, password, name FROM %s WHERE id='testId'", TestEntity.getTableName())
        );
    }

}

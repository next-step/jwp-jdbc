package core.nickbernate.action;

import core.nickbernate.TestEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityUpdateActionTest {

    @DisplayName("Update Query 생성하기")
    @Test
    void createSelectQuery() {
        /* given */
        TestEntity testEntity = new TestEntity("testId", "testName");

        /* when */
        EntityUpdateAction entityUpdateAction = new EntityUpdateAction(testEntity);

        /* then */
        assertThat(entityUpdateAction.getQuery()).isEqualTo(
                String.format("UPDATE %s SET name='testName' WHERE id='testId'", TestEntity.getTableName())
        );
    }

}

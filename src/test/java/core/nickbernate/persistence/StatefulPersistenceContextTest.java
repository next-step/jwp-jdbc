package core.nickbernate.persistence;

import core.jdbc.ConnectionManager;
import core.nickbernate.TestEntity;
import core.nickbernate.action.EntityAction;
import core.nickbernate.session.EntityKey;
import core.nickbernate.session.NickbernateSession;
import core.nickbernate.util.EntityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StatefulPersistenceContextTest {

    @DisplayName("entity와 snapshot 전부를 비교하여 updateAction 생성하기")
    @Test
    void generateActionsWithdirtyChecking() {
        /* given */
        PersistenceContext persistenceContext = new StatefulPersistenceContext(new NickbernateSession(ConnectionManager.getDataSource()));

        TestEntity testEntity = new TestEntity("testId", "testName");
        EntityKey entityKey = EntityUtil.createEntityKeyFrom(testEntity);
        persistenceContext.addEntity(entityKey, testEntity);

        TestEntity testEntity2 = new TestEntity("testId2", "testName");
        EntityKey entityKey2 = EntityUtil.createEntityKeyFrom(testEntity2);
        persistenceContext.addEntity(entityKey2, testEntity2);

        testEntity.updateName("updatedName");

        /* when */
        List<EntityAction> actions = persistenceContext.generateActionsWithdirtyChecking();

        /* then */
        assertThat(actions).hasSize(1);
    }

}

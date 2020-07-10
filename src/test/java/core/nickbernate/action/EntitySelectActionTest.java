package core.nickbernate.action;

import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntitySelectActionTest {

    @DisplayName("Select Query 생성하기")
    @Test
    void createSelectQuery() {
        /* given */
        Class<User> entityClass = User.class;
        String entityId = "testId";

        /* when */
        EntitySelectAction selectAction = new EntitySelectAction(entityClass, entityId);

        /* then */
        assertThat(selectAction.getQuery()).isEqualTo("SELECT userId, password, name, email FROM users WHERE userId='testId'");
    }

}

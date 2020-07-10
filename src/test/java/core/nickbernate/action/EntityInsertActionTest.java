package core.nickbernate.action;

import core.nickbernate.annotation.Entity;
import next.model.User;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityInsertActionTest {

    @DisplayName("Insert Query 생성하기")
    @Test
    void createSelectQuery() {
        /* given */
        User user = new User("testId", "password", "name", "javajigi@email.com");

        /* when */
        EntityInsertAction entityInsertAction = new EntityInsertAction(user);

        /* then */
        assertThat(entityInsertAction.getQuery()).isEqualTo(String.format("INSERT INTO %s VALUES ('testId', 'password', 'name', 'javajigi@email.com')", getEntityTableName(User.class)));

    }

    private String getEntityTableName(Class<?> entityClass) {
        Entity entityClassAnnotation = entityClass.getAnnotation(Entity.class);

        return StringUtils.isEmpty(entityClassAnnotation.table()) ? entityClass.getSimpleName() : entityClassAnnotation.table();
    }

}

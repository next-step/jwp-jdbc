package core.nickbernate;

import core.nickbernate.annotation.Entity;
import core.nickbernate.annotation.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@NoArgsConstructor
@Entity(table = "tests")
public class TestEntity {

    @Id
    private String id;

    private String name;

    public TestEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void updateName(String updatedName) {
        this.name = updatedName;
    }

    public static String getTableName() {
        Entity entityClassAnnotation = TestEntity.class.getAnnotation(Entity.class);

        return StringUtils.isEmpty(entityClassAnnotation.table()) ? TestEntity.class.getSimpleName() : entityClassAnnotation.table();
    }

}

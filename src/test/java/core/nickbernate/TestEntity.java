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

    private String password;
    private String name;

    public TestEntity(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public void update(String password, String name) {
        this.password = password;
        this.name = name;
    }

    public boolean isSameTestEntity(TestEntity testEntity) {
        return this.id.equals(testEntity.id);
    }

    public static String getTableName() {
        Entity entityClassAnnotation = TestEntity.class.getAnnotation(Entity.class);

        return StringUtils.isEmpty(entityClassAnnotation.table()) ? TestEntity.class.getSimpleName() : entityClassAnnotation.table();
    }

}

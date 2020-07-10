package core.nickbernate.util;

import core.nickbernate.TestEntity;
import core.nickbernate.annotation.Entity;
import core.nickbernate.annotation.Id;
import core.nickbernate.session.EntityKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntityUtilTest {

    @DisplayName("Entity Id 가져오기")
    @Test
    void findId() {
        /* given */
        String testId = "testId";
        TestEntity testEntity = new TestEntity(testId, "testName");

        /* when */
        EntityKey actual = EntityUtil.createEntityKeyFrom(testEntity);

        /* then */
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(new EntityKey(testEntity.getClass(), testId));
    }

    @DisplayName("Entity annotation이 존재하지 않으면 Exception")
    @Test
    void findId2() {
        /* given */
        String expected = "testId";
        TestClass testClass = new TestClass(expected, "testName");

        /* when */ /* then */
        assertThrows(IllegalArgumentException.class, () -> EntityUtil.createEntityKeyFrom(testClass));
    }

    @DisplayName("Id annotation이 존재하지 않으면 Exception")
    @Test
    void findId3() {
        /* given */
        String expected = "testId";
        TestClass2 testClass2 = new TestClass2(expected, "testName");

        /* when */ /* then */
        assertThrows(IllegalArgumentException.class, () -> EntityUtil.createEntityKeyFrom(testClass2));
    }

    @DisplayName("Id annotation이 여러 개 존재하면 Exception")
    @Test
    void findId4() {
        /* given */
        String expected = "testId";
        TestClass3 testClass3 = new TestClass3(expected, "testName");

        /* when */ /* then */
        assertThrows(IllegalArgumentException.class, () -> EntityUtil.createEntityKeyFrom(testClass3));
    }

    @DisplayName("entity와 snapshot의 모든 field가 같으면 true")
    @Test
    void isAllSameEntityFieldValues() {
        /* given */
        TestEntity testEntity = new TestEntity("testId", "testName");
        TestEntity snapShot = new TestEntity("testId", "testName");

        /* when */
        boolean result = EntityUtil.isAllSameEntityFieldValues(testEntity, snapShot);

        /* then */
        assertThat(result).isTrue();
    }

    @DisplayName("entity와 snapshot field가 하나라도 값이 다르면 false")
    @Test
    void isAllSameEntityFieldValues2() {
        /* given */
        TestEntity testEntity = new TestEntity("testId", "testName");
        TestEntity snapShot = new TestEntity("testId", "updatedName");

        /* when */
        boolean result = EntityUtil.isAllSameEntityFieldValues(testEntity, snapShot);

        /* then */
        assertThat(result).isFalse();
    }

    static class TestClass {

        private String id;
        private String name;

        public TestClass(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Entity
    static class TestClass2 {

        private String id;
        private String name;

        public TestClass2(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Entity
    static class TestClass3 {

        @Id
        private String id;

        @Id
        private String name;

        public TestClass3(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

}

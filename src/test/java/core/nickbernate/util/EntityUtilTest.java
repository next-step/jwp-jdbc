package core.nickbernate.util;

import core.nickbernate.annotation.Entity;
import core.nickbernate.annotation.Id;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntityUtilTest {

    @DisplayName("Entity Id 가져오기")
    @Test
    void findId() {
        /* given */
        String expected = "testId";
        TestEntity testEntity = new TestEntity(expected, "testName");

        /* when */
        Object actual = EntityUtil.findIdFrom(testEntity);

        /* then */
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Entity annotation이 존재하지 않으면 Exception")
    @Test
    void findId2() {
        /* given */
        String expected = "testId";
        TestClass testClass = new TestClass(expected, "testName");

        /* when */ /* then */
        assertThrows(IllegalArgumentException.class, () -> EntityUtil.findIdFrom(testClass));
    }

    @DisplayName("Id annotation이 존재하지 않으면 Exception")
    @Test
    void findId3() {
        /* given */
        String expected = "testId";
        TestClass2 testClass2 = new TestClass2(expected, "testName");

        /* when */ /* then */
        assertThrows(IllegalArgumentException.class, () -> EntityUtil.findIdFrom(testClass2));
    }

    @DisplayName("Id annotation이 여러 개 존재하면 Exception")
    @Test
    void findId4() {
        /* given */
        String expected = "testId";
        TestClass3 testClass3 = new TestClass3(expected, "testName");

        /* when */ /* then */
        assertThrows(IllegalArgumentException.class, () -> EntityUtil.findIdFrom(testClass3));
    }

    @Getter
    @Entity
    static class TestEntity {

        @Id
        private String id;

        private String name;

        public TestEntity(String id, String name) {
            this.id = id;
            this.name = name;
        }
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

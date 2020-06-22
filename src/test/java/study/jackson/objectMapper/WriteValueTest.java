package study.jackson.objectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.jackson.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class WriteValueTest {

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("기본 생성자로 ObjectMapper 객체를 생성할 수 있다")
    void create() {
        new ObjectMapper();
    }

    @Test
    @DisplayName("객체 타입의 필드가 null인 경우 null로 대입한다")
    void writeEmptyObject() throws JsonProcessingException {
        final Car car = new Car();
        car.setColor("red");

        final String result = mapper.writeValueAsString(car);

        assertThat(result).startsWith("{");
        assertThat(result).contains("\"color\":\"red\"");
        assertThat(result).contains("\"type\":null");
        assertThat(result).endsWith("}");
    }

    @Test
    @DisplayName("객체 타입의 필드에 값이 존재하는 경우 해당 필드들을 대입한다")
    void write() throws JsonProcessingException {
        final Car car = new Car();
        car.setColor("red");
        car.setType("audi");

        final String result = mapper.writeValueAsString(car);

        assertThat(result).startsWith("{");
        assertThat(result).contains("\"color\":\"red\"");
        assertThat(result).contains("\"type\":\"audi\"");
        assertThat(result).endsWith("}");
    }

    @Test
    @DisplayName("primitive 타입의 필드가 초기화 되지 않은 경우 0으로 대입한다")
    void writeEmptyPrimitive() throws JsonProcessingException {
        final PrimitiveClass primitive = new PrimitiveClass();

        final String result = mapper.writeValueAsString(primitive);

        assertThat(result).startsWith("{");
        assertThat(result).contains("\"byte1\":0");
        assertThat(result).contains("\"short1\":0");
        assertThat(result).contains("\"int1\":0");
        assertThat(result).contains("\"long1\":0");
        assertThat(result).contains("\"float1\":0");
        assertThat(result).contains("\"double1\":0.0");
        assertThat(result).contains("\"char1\":\"\\u0000\"");
        assertThat(result).contains("\"boolean1\":false");
        assertThat(result).endsWith("}");
    }

    @Test
    @DisplayName("Array 타입의 필드가 null인 경우 null을 대입한다")
    void arrayFieldNull() throws JsonProcessingException {
        final ArrayClass array = new ArrayClass();

        final String result = mapper.writeValueAsString(array);

        assertThat(result).startsWith("{");
        assertThat(result).contains("\"array\":null");
        assertThat(result).endsWith("}");
    }

    @Test
    @DisplayName("Array 타입의 필드에 값이 비어있는 경우 [] 을 대입한다")
    void arrayFieldEmpty() throws JsonProcessingException {
        final ArrayClass array = new ArrayClass(new int[]{});

        final String result = mapper.writeValueAsString(array);

        assertThat(result).startsWith("{");
        assertThat(result).contains("\"array\":[]");
        assertThat(result).endsWith("}");
    }

    @Test
    @DisplayName("Array 타입의 필드에 값이 존재하는 경우 값 들을 대입한다")
    void arrayField() throws JsonProcessingException {
        final int[] arr = new int[]{1, 2, 3};
        final ArrayClass array = new ArrayClass(arr);

        final String result = mapper.writeValueAsString(array);

        assertThat(result).startsWith("{");
        assertThat(result).contains("\"array\":[1,2,3]");
        assertThat(result).endsWith("}");
    }

    @Test
    @DisplayName("Collection 타입의 필드가 null인 경우 null을 대입한다")
    void collectionFieldNull() throws JsonProcessingException {
        final CollectionClass collection = new CollectionClass();

        final String result = mapper.writeValueAsString(collection);

        assertThat(result).startsWith("{");
        assertThat(result).contains("\"list\":null");
        assertThat(result).contains("\"set\":null");
        assertThat(result).contains("\"queue\":null");
        assertThat(result).endsWith("}");
    }

    @Test
    @DisplayName("Collection 타입의 필드에 값이 비어있는 경우 빈 배열을 대입한다")
    void collectionFieldEmpty() throws JsonProcessingException {
        final CollectionClass collection = new CollectionClass(new ArrayList(), new HashSet(), new LinkedList());

        final String result = mapper.writeValueAsString(collection);

        assertThat(result).startsWith("{");
        assertThat(result).contains("\"list\":[]");
        assertThat(result).contains("\"set\":[]");
        assertThat(result).contains("\"queue\":[]");
        assertThat(result).endsWith("}");
    }

    @Test
    @DisplayName("Collection 타입의 필드에 값이 존재하는 경우 빈 배열을 대입한다")
    void collectionField() throws JsonProcessingException {
        // given
        final Integer[] array = new Integer[]{1, 2, 3};

        final List<Integer> list = Arrays.asList(array);
        final Set<Integer> set = new HashSet<>(list);
        final Queue<Integer> queue = new LinkedList<>(list);

        final CollectionClass collection = new CollectionClass(list, set, queue);

        // when
        final String result = mapper.writeValueAsString(collection);

        // then
        assertThat(result).startsWith("{");
        assertThat(result).contains("\"list\":[1,2,3]");
        assertThat(result).contains("\"set\":[1,2,3]");
        assertThat(result).contains("\"queue\":[1,2,3]");
        assertThat(result).endsWith("}");
    }

    @Test
    @DisplayName("Map 타입의 필드가 null인 경우 null을 대입한다")
    void mapFieldNull() throws JsonProcessingException {
        final MapClass map = new MapClass();

        final String result = mapper.writeValueAsString(map);

        assertThat(result).startsWith("{");
        assertThat(result).contains("\"map\":null");
        assertThat(result).endsWith("}");
    }

    @Test
    @DisplayName("Map 타입의 필드에 값이 비어있는 경우 빈 객체를 대입한다")
    void mapFieldEmpty() throws JsonProcessingException {
        final MapClass map = new MapClass(new HashMap());

        final String result = mapper.writeValueAsString(map);

        assertThat(result).startsWith("{");
        assertThat(result).contains("\"map\":{}");
        assertThat(result).endsWith("}");
    }

    @Test
    @DisplayName("Map 타입의 필드에 값이 존재하는 경우 객체 리터럴로 대입한다")
    void mapField() throws JsonProcessingException {
        final HashMap hashMap = new HashMap();
        hashMap.put("a", "b");
        hashMap.put("c", "d");
        final MapClass map = new MapClass(hashMap);

        final String result = mapper.writeValueAsString(map);

        assertThat(result).startsWith("{");
        assertThat(result).contains("\"map\":{\"a\":\"b\",\"c\":\"d\"}");
        assertThat(result).endsWith("}");
    }

}
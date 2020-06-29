package study.jackson.objectMapper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import study.jackson.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ReadValueTest {

    private static ObjectMapper mapper;

    @BeforeAll
    static void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("기본 생성자로 ObjectMapper 객체를 생성할 수 있다")
    void create() {
        new ObjectMapper();
    }

    @Test
    @DisplayName("정상적인 json 문자열의 경우 객체의 각 필드에 데이터가 저장된다")
    void toObject() throws JsonProcessingException {
        final String carStr = "{\"color\": \"red\", \"type\": \"audi\"}";
        final String expectedColor = "red";
        final String expectedType = "audi";

        final Car result = mapper.readValue(carStr, Car.class);

        assertThat(result.getColor()).isEqualTo(expectedColor);
        assertThat(result.getType()).isEqualTo(expectedType);
    }

    @DisplayName("유효하지 않은 json 문자열의 경우 에러를 반환한다")
    @ParameterizedTest
    @ValueSource(strings = { "}", "{", "{1:}", "{:1}", "{a::a}", "{{a:a}" })
    void toObject(String str) {
        final Throwable thrown = catchThrowable(() -> mapper.readValue(str, Car.class));

        assertThat(thrown)
                .isInstanceOf(JsonParseException.class)
                .hasMessageContaining(str);
    }

    @Test
    @DisplayName("json 문자열에서 입력받지 못한 클래스의 Object 타입의 필드가 존재할 경우 null을 대입한다")
    void objectField() throws JsonProcessingException {
        final String carStr = "{\"color\": \"red\"}";

        final Car result = mapper.readValue(carStr, Car.class);

        assertThat(result.getType()).isEqualTo(null);
    }

    @Test
    @DisplayName("json 문자열에서 입력받지 못한 클래스의 primitive 타입의 필드가 존재할 경우 0을 대입한다")
    void primitiveField() throws JsonProcessingException {
        final PrimitiveClass result = mapper.readValue("{}", PrimitiveClass.class);

        assertThat(result.getByte1()).isEqualTo((byte) 0);
        assertThat(result.getChar1()).isEqualTo((char) 0);
        assertThat(result.getDouble1()).isEqualTo((double) 0);
        assertThat(result.getFloat1()).isEqualTo((float) 0);
        assertThat(result.getInt1()).isEqualTo((int) 0);
        assertThat(result.getLong1()).isEqualTo((long) 0);
        assertThat(result.getShort1()).isEqualTo((short) 0);
    }

    @Test
    @DisplayName("json 문자열에서 입력받지 못한 클래스의 Array 타입의 필드가 존재할 경우 null을 대입한다")
    void arrayField() throws JsonProcessingException {
        final ArrayClass result = mapper.readValue("{}", ArrayClass.class);

        assertThat(result.getArray()).isEqualTo(null);
    }

    @Test
    @DisplayName("json 문자열에서 입력받지 못한 클래스의 Collection 타입의 필드가 존재할 경우 null을 대입한다")
    void collectionField() throws JsonProcessingException {
        final CollectionClass result = mapper.readValue("{}", CollectionClass.class);

        assertThat(result.getList()).isEqualTo(null);
        assertThat(result.getSet()).isEqualTo(null);
        assertThat(result.getQueue()).isEqualTo(null);
    }

    @Test
    @DisplayName("json 문자열에서 입력받지 못한 클래스의 Map 타입의 필드가 존재할 경우 null을 대입한다")
    void mapField() throws JsonProcessingException {
        final MapClass result = mapper.readValue("{}", MapClass.class);

        assertThat(result.getMap()).isEqualTo(null);
    }

    @Test
    @DisplayName("json 문자열의 키 중 일부가 매핑할 클래스의 필드에 존재하지 않을 경우 에러를 반환한다")
    void notExistKey() {
        final String carStr = String.format("{\"color11\": \"red\"}");

        final Throwable thrown = catchThrowable(() -> mapper.readValue(carStr, Car.class));

        assertThat(thrown)
                .isInstanceOf(UnrecognizedPropertyException.class)
                .hasMessageContaining("Unrecognized field")
                .hasMessageContaining(carStr);
    }

    @Test
    @DisplayName("문자열인 json 문자열의 키가 Collection 타입의 필드에 매핑될 경우 에러를 반환한다")
    void stringToCollection() {
        final String str = String.format("{\"list\": \"list\"}");

        final Throwable thrown = catchThrowable(() -> mapper.readValue(str, CollectionClass.class));

        assertThat(thrown)
                .isInstanceOf(MismatchedInputException.class)
                .hasMessageContaining("Cannot deserialize instance of");
    }

    @Test
    @DisplayName("배열인 json 문자열의 키가 Collection 타입의 필드에 매핑될 경우 에러를 반환한다")
    void arrayToCollection() throws JsonProcessingException {
        final String str = String.format("{\"list\": [\"list\"]}");

        final CollectionClass collectionInstance = mapper.readValue(str, CollectionClass.class);

        assertThat(collectionInstance.getList()).containsExactly("list");
    }

}

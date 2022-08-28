package core.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RowMapperFunctionTest {

    @DisplayName("ResultSet 을 원하는 User 타입으로 반환한다")
    @Test
    void return_to_the_user_type() throws SQLException {
        // given
        final MockResultSet mockResultSet = createMockResultSet();

        final RowMapperFunction<Optional<User>> function = (rs) -> {
            if (rs.next()) {
                return Optional.of(new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email")
                ));
            }
            return Optional.empty();
        };

        final User expected = new User("admin", "password", "자바지기", "admin@slipp.net");

        // when
        final Optional<User> actual = function.apply(mockResultSet);

        // then
        assertThat(actual).isPresent()
            .hasValue(expected);
    }

    @DisplayName("ResultSet 을 String 타입으로 반환한다")
    @ParameterizedTest(name = "data {0} is {1}")
    @MethodSource
    void return_to_the_string_type(ResultSet resultSet, String expected) throws SQLException {
        // given
        final RowMapperFunction<String> function = (rs) -> {
            if (rs.next()) {
                return rs.getString("userId");
            }
            return null;
        };

        // when
        final String actual = function.apply(resultSet);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> return_to_the_string_type() {
        return Stream.of(
            Arguments.of(createMockResultSet(), "admin"),
            Arguments.of(new MockResultSet(Collections.emptyList()), null)
        );
    }

    @DisplayName("ResultSet 을 List<String> 타입으로 반환한다")
    @Test
    void return_to_the_list_type() throws SQLException {
        // given
        Map<String, Object> secondRow = new HashMap<>();
        secondRow.put("userId", "tester");
        final MockResultSet resultSet = createMockResultSet(secondRow);

        final RowMapperFunction<List<String>> function = (rs) -> {
            List<String> userIds = new ArrayList<>();
            while (rs.next()) {
                userIds.add(rs.getString("userId"));
            }
            return userIds;
        };

        final List<String> expected = Arrays.asList("admin", "tester");

        // when
        final List<String> actual = function.apply(resultSet);

        // then
        assertThat(actual).isEqualTo(expected);
    }


    private static MockResultSet createMockResultSet() {
        return createMockResultSet(null);
    }

    private static MockResultSet createMockResultSet(Map<String, Object> newRow) {
        List<Map<String, Object>> resultRows = new ArrayList<>();
        Map<String, Object> resultRow = new HashMap<>();
        resultRow.put("userId", "admin");
        resultRow.put("password", "password");
        resultRow.put("name", "자바지기");
        resultRow.put("email", "admin@slipp.net");
        resultRows.add(resultRow);
        if (newRow != null && !newRow.isEmpty()) {
            resultRows.add(newRow);
        }
        return new MockResultSet(resultRows);
    }
}

package core.jdbc;

import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("값 매핑")
class RowMapperTest {

    @Test
    @DisplayName("클래스로 생성")
    void instance() {
        assertThatNoException().isThrownBy(() -> new RowMapper<>(User.class));
    }

    @Test
    @DisplayName("결과 값으로 매핑")
    void map() throws SQLException {
        //given
        ResultSet resultSet = mock(ResultSet.class);
        String userId = "userId";
        when(resultSet.getObject("userId")).thenReturn(userId);
        String password = "password";
        when(resultSet.getObject("password")).thenReturn(password);
        String name = "name";
        when(resultSet.getObject("name")).thenReturn(name);
        String email = "email@email";
        when(resultSet.getObject("email")).thenReturn(email);
        //when
        User user = new RowMapper<>(User.class).map(resultSet);
        //then
        assertThat(user).isEqualTo(new User(userId, password, name, email));
    }
}

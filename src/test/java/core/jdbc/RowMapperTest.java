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
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email@email";
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getObject("userId")).thenReturn(userId);
        when(resultSet.getObject("password")).thenReturn(password);
        when(resultSet.getObject("name")).thenReturn(name);
        when(resultSet.getObject("email")).thenReturn(email);
        //when
        User user = new RowMapper<>(User.class).map(resultSet);
        //then
        assertThat(user).isEqualTo(new User(userId, password, name, email));
    }
}

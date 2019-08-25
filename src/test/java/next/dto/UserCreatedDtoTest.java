package next.dto;

import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserCreatedDtoTest {
    @DisplayName("User객체 생성")
    @Test
    void create() {
        //given
        UserCreatedDto dto = new UserCreatedDto("user", "pw", "pobi", "haha@gmail.com");

        //when
        User actual = dto.toEntity();

        //then
        assertThat(actual.getUserId()).isEqualTo(dto.getUserId());
        assertThat(actual.getPassword()).isEqualTo(dto.getPassword());
        assertThat(actual.getName()).isEqualTo(dto.getName());
        assertThat(actual.getEmail()).isEqualTo(dto.getEmail());
    }
}
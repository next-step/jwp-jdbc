package next.model;

import core.db.DataBase;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    @DisplayName("User생성 - dto객체 이용")
    @Test
    void create() {
        UserCreatedDto dto = new UserCreatedDto("id", "pw", "nm", "mail@gmail.com");
        User user = dto.toEntity();

        assertThat(user.getUserId()).isEqualTo(dto.getUserId());
        assertThat(user.getPassword()).isEqualTo(dto.getPassword());
        assertThat(user.getName()).isEqualTo(dto.getName());
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
    }
}
package next.dto;

import core.db.DataBase;
import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserUpdatedDtoTest {
    @DisplayName("User update - dto객체 이용")
    @Test
    void update() {
        //given
        User befUser = new User("userId", "password", "name", "test@mail.com");
        DataBase.addUser(befUser);
        UserUpdatedDto dto = new UserUpdatedDto("update", "update@mail.com");

        //when
        User targetUser = DataBase.findUserById(befUser.getUserId());
        dto.update(targetUser);

        //then
        assertThat(targetUser.getUserId()).isEqualTo(befUser.getUserId());
        assertThat(targetUser.getPassword()).isEqualTo(befUser.getPassword());
        assertThat(targetUser.getName()).isEqualTo(dto.getName());
        assertThat(targetUser.getEmail()).isEqualTo(dto.getEmail());
    }
}
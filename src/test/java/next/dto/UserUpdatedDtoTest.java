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
        User expected = DataBase.findUserById(befUser.getUserId());
        expected.update(dto.getName(), dto.getEmail());
        DataBase.updateById(expected);
        User actual = DataBase.findUserById(expected.getUserId());


        //then
        assertThat(expected.getUserId()).isEqualTo(actual.getUserId());
        assertThat(expected.getPassword()).isEqualTo(actual.getPassword());
        assertThat(expected.getName()).isEqualTo(actual.getName());
        assertThat(expected.getEmail()).isEqualTo(actual.getEmail());
    }
}
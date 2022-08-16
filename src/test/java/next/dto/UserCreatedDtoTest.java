package next.dto;

import next.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserCreatedDtoTest {

    @Test
    void toUser() {
        UserCreatedDto userCreatedDto = new UserCreatedDto("userId", "비밀번호", "이름", "jordy@mint");
        User actual = userCreatedDto.toUser();
        assertThat(actual).isEqualTo(
                new User(
                        "userId",
                        "비밀번호",
                        "이름",
                        "jordy@mint"
                )
        );
    }
}
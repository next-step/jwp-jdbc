package next.dto;

import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserCreatedDtoTest {

    @DisplayName("회원가입 요청 정보 Dto를 User 도메인으로 변경한다.")
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

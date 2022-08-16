package next.dto;

import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserUpdatedDtoTest {

    @DisplayName("회원정보 업데이트 요청정보  Dto를 User 도메인으로 변경한다.")
    @Test
    void toUser() {
        UserUpdatedDto updatedDto = new UserUpdatedDto("포비", "conan@next-level");
        User actual = updatedDto.toUser();
        assertThat(actual).isEqualTo(
                new User(
                        null,
                        null,
                        "포비",
                        "conan@next-level"
                )
        );
    }
}

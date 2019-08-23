package next.controller;

import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptance {

    private static final Logger logger = LoggerFactory.getLogger(UserAcceptanceTest.class);

    private static final String URL_USERS = "/api/users";

    @DisplayName("사용자 회원가입/조회/수정/삭제")
    @Test
    void crud() {
        // 회원가입
        UserCreatedDto expected =
                new UserCreatedDto("pobi", "password", "포비", "pobi@nextstep.camp");
        EntityExchangeResult<byte[]> response = create(URL_USERS, expected, UserCreatedDto.class);

        URI location = response.getResponseHeaders().getLocation();
        logger.debug("location : {}", location);

        // 조회
        User actual = get(location, User.class).getResponseBody();
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());

        // 수정
        UserUpdatedDto updateExpectedUser = new UserUpdatedDto("코난", "conan@nextstep.camp");
        EntityExchangeResult<User> updateResult = put(location, updateExpectedUser, UserUpdatedDto.class, User.class);
        actual = get(location, User.class).getResponseBody();

        assertThat(updateResult.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getName()).isEqualTo(updateExpectedUser.getName());
        assertThat(actual.getEmail()).isEqualTo(updateExpectedUser.getEmail());
    }
}
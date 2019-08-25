package next.controller;

import next.dto.UserCreatedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest {
    private static final Logger logger = LoggerFactory.getLogger(UserAcceptanceTest.class);
    private static final String USERS_API_URL = "/api/users";

    private static WebTestClient client() {
        return WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080")
                .build();
    }

    @Test
    @DisplayName("사용자 회원가입/조회/수정/삭제")
    void crud() {
        // 회원가입
        final UserCreatedDto userCreatedDto =
                new UserCreatedDto("pobi", "password", "포비", "pobi@nextstep.camp");
        final EntityExchangeResult<byte[]> response = UserAcceptanceTest.client()
                .post()
                .uri(USERS_API_URL)
                .body(Mono.just(userCreatedDto), UserCreatedDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .returnResult();

        final URI location = response.getResponseHeaders().getLocation();
        assertThat(location).isEqualTo(URI.create(USERS_API_URL + "?userId=" + userCreatedDto.getUserId()));

        // 조회
//        User actual = client()
//                .get()
//                .uri(location.toString())
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(User.class)
//                .returnResult().getResponseBody();
//        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
//        assertThat(actual.getName()).isEqualTo(expected.getName());
//        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
//
//        // 수정
//        UserUpdatedDto updateUser = new UserUpdatedDto("코난", "conan@nextstep.camp");
//        client()
//                .put()
//                .uri(location.toString())
//                .body(Mono.just(updateUser), UserUpdatedDto.class)
//                .exchange()
//                .expectStatus().isOk();
//
//
//        actual = client()
//                .get()
//                .uri(location.toString())
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(User.class)
//                .returnResult().getResponseBody();
//        assertThat(actual.getName()).isEqualTo(updateUser.getName());
//        assertThat(actual.getEmail()).isEqualTo(updateUser.getEmail());
    }
}

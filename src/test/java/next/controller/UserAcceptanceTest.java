package next.controller;

import lombok.extern.slf4j.Slf4j;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class UserAcceptanceTest {

    @Test
    @DisplayName("사용자 회원가입/조회/수정/삭제")
    void crud() {
        // 회원가입
        UserCreatedDto expected = UserCreatedDto.builder()
                .userId("pobi")
                .password("password")
                .name("포비")
                .email("pobi@nextstep.camp")
                .build();

        EntityExchangeResult<byte[]> response = client()
                .post()
                .uri("/api/users")
                .body(Mono.just(expected), UserCreatedDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .returnResult();
        URI location = response.getResponseHeaders().getLocation();
        log.debug("location : {}", location); // /api/users?userId=pobi 와 같은 형태로 반환

        // 조회
        User actual = client()
                .get()
                .uri(location.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .returnResult().getResponseBody();
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());

        // 수정
        UserUpdatedDto updateUser = UserUpdatedDto.builder()
                .name("코난")
                .email("conan@nextstep.camp")
                .build();

        client()
                .put()
                .uri(location.toString())
                .body(Mono.just(updateUser), UserUpdatedDto.class)
                .exchange()
                .expectStatus().isOk();


        actual = client()
                .get()
                .uri(location.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .returnResult().getResponseBody();
        assertThat(actual.getName()).isEqualTo(updateUser.getName());
        assertThat(actual.getEmail()).isEqualTo(updateUser.getEmail());
    }

    private WebTestClient client() {
        return WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080")
                .build();
    }
}

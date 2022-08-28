package next.controller;

import java.net.URISyntaxException;
import java.util.Objects;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest {
    private static final Logger logger = LoggerFactory.getLogger(UserAcceptanceTest.class);

    @Test
    @DisplayName("사용자 회원가입/조회/수정/삭제")
    void crud() throws URISyntaxException {
        // 세션 유지를 위한 쿠키
        final String jsessionid = Objects.requireNonNull(client()
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ResponseCookie.class)
                .getResponseCookies()
                .getFirst("JSESSIONID"))
            .getValue();
        logger.debug("jsessionid = {}", jsessionid);

        // 회원가입
        UserCreatedDto expected =
                new UserCreatedDto("pobi", "password", "포비", "pobi@nextstep.camp");
        EntityExchangeResult<byte[]> response = client()
                .post()
                .uri("/api/users")
                .cookie("JSESSIONID", jsessionid)
                .body(Mono.just(expected), UserCreatedDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .returnResult();
        URI location = response.getResponseHeaders().getLocation();
        logger.debug("location : {}", location); // /api/users?userId=pobi 와 같은 형태로 반환

        // 조회
        User actual = client()
                .get()
                .uri(location.toString())
                .cookie("JSESSIONID", jsessionid)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .returnResult().getResponseBody();
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());

        // 로그인
        client()
            .post()
            .uri("/users/login?userId=pobi&password=password")
            .cookie("JSESSIONID", jsessionid)
            .exchange()
            .expectStatus().isFound();

        // 수정
        UserUpdatedDto updateUser = new UserUpdatedDto("코난", "conan@nextstep.camp");
        client()
                .put()
                .uri(location.toString())
                .cookie("JSESSIONID", jsessionid)
                .body(Mono.just(updateUser), UserUpdatedDto.class)
                .exchange()
                .expectStatus().isOk();


        actual = client()
                .get()
                .uri(location.toString())
                .cookie("JSESSIONID", jsessionid)
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

package next.controller;

import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.net.URI;

public abstract class AbstractAcceptanceTest {

    protected <T> URI post(String uri, T expected, Class<T> clazz) {
        return client()
                .post()
                .uri(uri)
                .body(Mono.just(expected), clazz)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .returnResult()
                .getResponseHeaders()
                .getLocation();
    }

    protected <T> T get(String uri, Class<T> clazz) {
        return client()
                .get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBody(clazz)
                .returnResult().getResponseBody();
    }

    protected <T> void put(String uri, T user, Class<T> clazz) {
        client()
                .put()
                .uri(uri)
                .body(Mono.just(user), clazz)
                .exchange()
                .expectStatus().isOk();

    }

    private WebTestClient client() {
        return WebTestClient
                .bindToServer()
                .baseUrl(getServerBaseUrl())
                .build();
    }

    protected abstract String getServerBaseUrl();

}

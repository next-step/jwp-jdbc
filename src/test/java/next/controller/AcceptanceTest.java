package next.controller;

import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.net.URI;

public class AcceptanceTest {
    public <T> EntityExchangeResult<byte[]> createResource(String url, T expected, Class<T> tClass) {
        return client()
                .post()
                .uri(url)
                .body(Mono.just(expected), tClass)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .returnResult();
    }

    public <T> T getResource(URI location, Class<T> tClass) {
        return client()
                .get()
                .uri(location.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(tClass)
                .returnResult().getResponseBody();
    }

    public <T> void updateResource(URI location, T update, Class<T> tClass) {
        client()
                .put()
                .uri(location.toString())
                .body(Mono.just(update), tClass)
                .exchange()
                .expectStatus().isOk();
    }

    private WebTestClient client() {
        return WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080")
                .build();
    }
}

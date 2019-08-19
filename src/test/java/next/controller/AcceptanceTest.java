package next.controller;

import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.net.URI;

public class AcceptanceTest {
    protected WebTestClient client() {
        return WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080")
                .build();
    }

    protected <T> URI createResource(String url, T data, Class<T> clazz) {
        EntityExchangeResult<byte[]> response = client()
                .post()
                .uri(url)
                .body(Mono.just(data), clazz)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .returnResult();
        return response.getResponseHeaders().getLocation();
    }

    protected <T> T getResource(URI location, Class<T> clazz) {
        return client()
                .get()
                .uri(location.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(clazz)
                .returnResult().getResponseBody();
    }

    protected <T> void updateResource(URI location, T data, Class<T> clazz) {
        client()
                .put()
                .uri(location.toString())
                .body(Mono.just(data), clazz)
                .exchange()
                .expectStatus().isOk();
    }

}

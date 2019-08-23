package next.controller;

import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.net.URI;

class AbstractAcceptance {

    protected WebTestClient client() {
        return WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080")
                .build();
    }

    protected <T> EntityExchangeResult<byte[]> create(String url, T expected, Class<T> clazz) {
        return client()
                .post()
                .uri(url)
                .body(Mono.just(expected), clazz)
                .exchange()
                .expectBody()
                .returnResult();
    }

    protected <T> EntityExchangeResult<T> get(URI location, Class<T> expect) {
        return client()
                .get()
                .uri(location.toString())
                .exchange()
                .expectBody(expect)
                .returnResult();
    }

    protected <T, R> EntityExchangeResult<R> put(URI location, T requestObject, Class<T> requestClazz, Class<R> returnClazz) {
        return client()
                .put()
                .uri(location.toString())
                .body(Mono.just(requestObject), requestClazz)
                .exchange()
                .expectBody(returnClazz)
                .returnResult();
    }
}
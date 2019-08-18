package next.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Created by hspark on 2019-08-18.
 */
public abstract class AbstractAcceptanceTest {
    private static final WebTestClient CLIENT;

    static {
        CLIENT = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .build();
    }

    private static final Logger logger = LoggerFactory.getLogger(UserAcceptanceTest.class);

    public <T> String postResource(String url, T body, Class<T> clazz) {
        EntityExchangeResult<byte[]> response = CLIENT
                .post()
                .uri(url)
                .body(Mono.just(body), clazz)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .returnResult();

        URI location = response.getResponseHeaders().getLocation();
        logger.debug("location : {}", location);
        return location.toString();
    }

    public <T> T getResource(String url, Class<T> clazz) {
        return CLIENT
                .get()
                .uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(clazz)
                .returnResult().getResponseBody();

    }

    public <T> void putResource(String url, T body, Class<T> clazz) {
        CLIENT.put()
                .uri(url)
                .body(Mono.just(body), clazz)
                .exchange()
                .expectStatus().isOk();
    }
}

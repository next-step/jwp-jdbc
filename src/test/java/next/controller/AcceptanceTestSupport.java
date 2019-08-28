package next.controller;

import org.springframework.test.web.reactive.server.WebTestClient;

public abstract class AcceptanceTestSupport {

    protected WebTestClient client() {
        return WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080")
                .build();
    }
}

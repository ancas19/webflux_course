package co.com.ancas.playground.sec07;

import co.com.ancas.playground.sec07.DTO.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec08BearerAuthTest extends AbstractWebClient{
    private final WebClient client= createWebClient(b->b.defaultHeaders(h->h.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")));

    @Test
    void bearer() throws InterruptedException {
        client.get()
                .uri("/lec08/product/{id}",10)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}

package co.com.ancas.playground.sec07;

import co.com.ancas.playground.sec07.DTO.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec07BasicAuthTest extends AbstractWebClient{
    private final WebClient client= createWebClient(b->b.defaultHeaders(h->h.setBasicAuth("java","secret")));

    @Test
    void basicAuth() throws InterruptedException {
        client.get()
                .uri("/lec07/product/{id}",10)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}

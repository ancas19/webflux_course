package co.com.ancas.playground.sec07;

import co.com.ancas.playground.sec07.DTO.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.time.Duration;


public class Lec02FluxTest extends AbstractWebClient{

    private final WebClient client= createWebClient();
    @Test
    void streamGet() throws InterruptedException {
        client.get()
                .uri("/lec02/product/stream")
                .retrieve()
                .bodyToFlux(Product.class)
                .take(Duration.ofSeconds(3))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}

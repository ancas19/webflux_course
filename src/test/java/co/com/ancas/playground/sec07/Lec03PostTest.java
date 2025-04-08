package co.com.ancas.playground.sec07;

import co.com.ancas.playground.sec07.DTO.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec03PostTest extends AbstractWebClient{

    private final WebClient client= createWebClient();

    @Test
    void postBodyValue() throws InterruptedException {
        Product product=new Product(null,"Product 1",1000);
        client.post()
                .uri("/lec03/product")
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void postBody() throws InterruptedException {
        Mono<Product> product= Mono.fromSupplier(()->new Product(null,"Product 1",1000))
                .delayElement(Duration.ofSeconds(1));
        client.post()
                .uri("/lec03/product")
                .body(product,Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}

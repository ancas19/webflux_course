package co.com.ancas.playground.sec07;

import co.com.ancas.playground.sec07.DTO.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

public class Lec04HeaderTest extends AbstractWebClient{

    private final WebClient client= createWebClient(b->b.defaultHeader("caller-id","order-service"));

    @Test
    void defaultHeader() throws InterruptedException {
        client.get()
                .uri("/lec04/product/{id}",10)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }


    @Test
    void overrideHeader()  {
        client.get()
                .uri("/lec04/product/{id}",10)
                .header("caller-id","11")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void headersWithMap()  {
        Map<String,String> headers= Map.of("caller-id","11","order-id","10");
        client.get()
                .uri("/lec04/product/{id}",10)
                .headers(h->h.setAll(headers))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}

package co.com.ancas.playground.sec10;

import co.com.ancas.playground.sec10.DTO.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Lec02Http2Test extends AbstractWebClient {

    /*
        It is for demo purposes! You might NOT need to adjust all these
        If the response time 100ms => 500 / (100 ms) ==> 5000 req/sec
     */
    private final WebClient client = createWebClient(b -> {
        Integer poolSize = 20000;
        ConnectionProvider provider = ConnectionProvider.builder("ancas")
                .pendingAcquireMaxCount(poolSize)
                .lifo()
                .build();

        HttpClient httpClient = HttpClient.create(provider)
                .protocol(HttpProtocol.H2C)
                .compress(true)
                .keepAlive(true);

        b.clientConnector(new ReactorClientHttpConnector(httpClient));
    });

    @Test
    void concurrentRequest() {
        Integer max = 20000;
        Flux.range(1, max)
                .flatMap(this::getProduct, max)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(l -> assertEquals(l.size(), max))
                .expectComplete()
                .verify();
    }

    private Mono<Product> getProduct(Integer id) {
        return client.get()
                .uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}

package co.com.ancas.playground.sec10;

import co.com.ancas.playground.sec10.DTO.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;


import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Lec01HttpConnectionPoolingTest extends AbstractWebClient {

    /*
        It is for demo purposes! You might NOT need to adjust all these
        If the response time 100ms => 500 / (100 ms) ==> 5000 req/sec
     */
    private final WebClient client = createWebClient(b -> {
        Integer poolSize = 10000;
        ConnectionProvider provider = ConnectionProvider.builder("ancas")
                .maxConnections(poolSize)            // Set the max connections explicitly
                .maxIdleTime(Duration.ofSeconds(30)) // How long connections can stay idle
                .maxLifeTime(Duration.ofMinutes(5))  // Maximum connection lifetime
                .pendingAcquireTimeout(Duration.ofSeconds(60)) //
                .pendingAcquireMaxCount(poolSize * 5)
                .lifo()
                .build();

        HttpClient httpClient = HttpClient.create(provider)
                .compress(true)
                .keepAlive(true);

        b.clientConnector(new ReactorClientHttpConnector(httpClient));
    });

    @Test
    void concurrentRequest() {
        Integer max = 10000;
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

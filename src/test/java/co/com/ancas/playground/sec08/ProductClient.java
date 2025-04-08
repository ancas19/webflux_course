package co.com.ancas.playground.sec08;

import co.com.ancas.playground.sec08.dto.Product;
import co.com.ancas.playground.sec08.dto.UploadResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductClient {
    private WebClient webClient=WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build();


    public Mono<UploadResponse> uploladProducts(Flux<Product> products) {
        return webClient.post()
                .uri("/products/upload")
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(products,Product.class)
                .retrieve()
                .bodyToMono(UploadResponse.class);
    }
}

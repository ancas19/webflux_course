package co.com.ancas.playground.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/reactive")
public class ReactiveWebController {
    private static final Logger log= LoggerFactory.getLogger(ReactiveWebController.class);
    private final WebClient webClient= WebClient.builder()
            .baseUrl("http://localhost:7070")
            .build();


    @GetMapping("/products")
    public ResponseEntity<Flux<Product>> getProducts(){
       return ResponseEntity.ok( this.webClient
                .get()
                .uri("/demo01/products/notorious")
                .retrieve()
                .bodyToFlux(Product.class)
                .onErrorComplete()
                .doOnNext(product -> log.info("Product: {}", product))
       );
    }


    @GetMapping(value="/products/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<Product>> getProductsStream(){
        return ResponseEntity.ok( this.webClient
                .get()
                .uri("/demo01/products")
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(product -> log.info("Product: {}", product))
        );
    }
}

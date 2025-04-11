package co.com.ancas.playground.sec09.service;

import co.com.ancas.playground.sec09.dto.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class DataSetUpService implements CommandLineRunner {
    private final ProductService productService;

    public DataSetUpService(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        Flux.range(1,1000)
                .delayElements(java.time.Duration.ofSeconds(1))
                .map(i->new Product(null,"Product-"+i, ThreadLocalRandom.current().nextInt(1,100)))
                .flatMap(dto->productService.saveProduct(Mono.just(dto)))
                .subscribe();
    }
}

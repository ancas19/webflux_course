package co.com.ancas.playground.sec09.service;

import co.com.ancas.playground.sec09.dto.Product;
import co.com.ancas.playground.sec09.mapper.ProductMapper;
import co.com.ancas.playground.sec09.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final Sinks.Many<Product> sink;

    public ProductService(ProductRepository productRepository, Sinks.Many<Product> sink) {
        this.productRepository = productRepository;
        this.sink = sink;
    }


    public Mono<Product> saveProduct(Mono<Product> products){
        return products
                .map(ProductMapper::toProductEntity)
                .flatMap(productRepository::save)
                .map(ProductMapper::toProductDTO)
                .doOnNext(sink::tryEmitNext);
    }

    public Flux<Product> productStreeam(){
        return sink.asFlux();
    }

    public Flux<Product> getProducts() {
        return productRepository.findAll()
                .map(ProductMapper::toProductDTO);
    }

    public Mono<Long> getProductsCount() {
        return productRepository.count();
    }
}

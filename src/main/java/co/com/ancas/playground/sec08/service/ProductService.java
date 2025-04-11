package co.com.ancas.playground.sec08.service;

import co.com.ancas.playground.sec08.repository.ProductRepository;
import co.com.ancas.playground.sec08.dto.Product;
import co.com.ancas.playground.sec08.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Flux<Product> saveProducts(Flux<Product> products){
        return products
                .map(ProductMapper::toProductEntity)
                .as(productRepository::saveAll)
                .map(ProductMapper::toProductDTO);
    }

    public Flux<Product> getProducts() {
        return productRepository.findAll()
                .map(ProductMapper::toProductDTO);
    }

    public Mono<Long> getProductsCount() {
        return productRepository.count();
    }
}

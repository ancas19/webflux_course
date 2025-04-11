package co.com.ancas.playground.sec09.controller;

import co.com.ancas.playground.sec09.dto.Product;
import co.com.ancas.playground.sec09.dto.UploadResponse;
import co.com.ancas.playground.sec09.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static final Logger log= LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    public Mono<Product> uploadProducts(@RequestBody Mono<Product> products) {
        log.info("Invoked");
        return productService.saveProduct(products);
    }


    @GetMapping(value = "/stream/{maxPrice}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> getProducts(@PathVariable("maxPrice") Integer maxPrice) {
        return productService.productStreeam()
                .filter(product -> product.price()<= maxPrice);
    }



}

package co.com.ancas.playground.sec02;

import co.com.ancas.playground.sec02.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Lec02ProductRepositoryTest extends AbstractTest{
    private static final Logger log = LoggerFactory.getLogger(Lec02ProductRepositoryTest.class);

    @Autowired
    private ProductRepository productRepository;


    @Test
    void findByPriceBetween() {
        this.productRepository.findByPriceBetween(750, 1000)
                .doOnNext(product -> log.info("Product: {}", product))
                .as(StepVerifier::create)
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }


    @Test
    void findByPriceBetweenPageable() {
        this.productRepository.findBy(PageRequest.of(0,3).withSort(Sort.by("price").ascending()))
                .doOnNext(product -> log.info("Product: {}", product))
                .as(StepVerifier::create)
                .assertNext(p->assertEquals(200,p.getPrice()))
                .assertNext(p->assertEquals(250,p.getPrice()))
                .assertNext(p->assertEquals(300,p.getPrice()))
                .expectComplete()
                .verify();
    }
}

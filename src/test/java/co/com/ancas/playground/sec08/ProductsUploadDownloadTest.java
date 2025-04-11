package co.com.ancas.playground.sec08;

import co.com.ancas.playground.sec08.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.file.Path;
import java.time.Duration;

/*
Just for Demo
 */
public class ProductsUploadDownloadTest {
    private static final Logger log= LoggerFactory.getLogger(ProductsUploadDownloadTest.class);
    private final ProductClient client= new ProductClient();

    @Test
    void uipload() throws InterruptedException {

        Flux<Product> productFlux=Flux.range(1,1000000)
                .map(i->new Product(null,"Product "+i,i*1000)
        );

        client.uploladProducts(productFlux)
                .doOnNext(item->log.info("Receive: {} ",item))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }


    @Test
    void download() throws InterruptedException {
        client.getProducts()
                .map(Product::toString)
                .as(data->FileWriter.create(data, Path.of("products.txt")))
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}

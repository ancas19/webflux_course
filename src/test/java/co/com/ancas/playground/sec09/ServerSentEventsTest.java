package co.com.ancas.playground.sec09;

import co.com.ancas.playground.sec09.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec09")
public class ServerSentEventsTest {
    private static  final Logger log= LoggerFactory.getLogger(ServerSentEventsTest.class);
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void serverSentEvents(){
        webTestClient.get()
                .uri("/products/stream/80")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(Product.class)
                .getResponseBody()
                .take(5)
                .doOnNext(dto-> log.info("Product: {}", dto))
                .collectList()
                .as(StepVerifier::create)
                .assertNext(list->{
                    log.info("List: {}", list);
                    assertFalse(list.isEmpty());
                    assertEquals(5, list.size());
                    assertTrue(list.stream().allMatch(p->p.price()<=80));
                })
                .expectComplete()
                .verify();
    }
}


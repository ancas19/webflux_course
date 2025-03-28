package co.com.ancas.playground.sec03;

import co.com.ancas.playground.sec03.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec03")
public class CustomerServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private WebTestClient webClient;

    @Test
    void allCustomer(){
        this.webClient.get()
                .uri("/customers")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerDTO.class)
                .value(customers -> LOGGER.info("Customers: {}" ,customers))
                .hasSize(10);
    }


    @Test
    void allCustomerPaginated(){
        this.webClient.get()
                .uri("/customers/pageable?page=0&size=3")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(response -> LOGGER.info("{}",new String(response.getResponseBody())))
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[2].id").isEqualTo(3);
    }

}

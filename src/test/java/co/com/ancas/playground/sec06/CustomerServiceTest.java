package co.com.ancas.playground.sec06;

import co.com.ancas.playground.sec06.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec06")
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

    @Test
    void customerById(){
        this.webClient.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(response-> LOGGER.info("{}",new String(Objects.requireNonNull(response.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("sam")
                .jsonPath("$.email").isEqualTo("sam@gmail.com");
    }

    @Test
    void createAndDeleteCustomer(){
        this.webClient.post()
                .uri("/customers")
                .bodyValue(new CustomerDTO(null,"test","test@gmail"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(response-> LOGGER.info("{}",new String(Objects.requireNonNull(response.getResponseBody()))))
                .jsonPath("$.name").isEqualTo("test")
                .jsonPath("$.email").isEqualTo("test@gmail")
                .jsonPath("$.id").isEqualTo(11);

        this.webClient.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .isEmpty();

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
    void updateCustomer(){
        this.webClient.put()
                .uri("/customers/1")
                .bodyValue(new CustomerDTO(1L,"test","test@gmail"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(response-> LOGGER.info("{}",new String(Objects.requireNonNull(response.getResponseBody()))))
                .jsonPath("$.name").isEqualTo("test")
                .jsonPath("$.email").isEqualTo("test@gmail")
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void customerNotFound(){
        //Customer not found
        this.webClient.get()
                .uri("/customers/11")
                .exchange()
                .expectStatus().isNotFound();

        //Delete customer
        this.webClient.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus()
                .isNotFound();
        // Put
        this.webClient.put()
                .uri("/customers/11")
                .bodyValue(new CustomerDTO(1L,"test","test@gmail"))
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void invalidArgument(){
        //Missing name
        this.webClient.post()
                .uri("/customers")
                .bodyValue(new CustomerDTO(null,null,"test@gmail"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(response-> LOGGER.info("{}",new String(Objects.requireNonNull(response.getResponseBody()))));
        //Missing email
        this.webClient.post()
                .uri("/customers")
                .bodyValue(new CustomerDTO(null,"test",null))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .consumeWith(response-> LOGGER.info("{}",new String(Objects.requireNonNull(response.getResponseBody()))));
    }
}

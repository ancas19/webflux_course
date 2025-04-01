package co.com.ancas.playground.sec05;

import co.com.ancas.playground.sec05.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec05")
public class CustomerServiceTest {

    @Autowired
    private WebTestClient webClient;
    /*
    Just validate HTTP response status codes
    unauthorized - no token
    unauthorized - invalid token
    standard category - GET success
    standard category - POST/PUT/DELETE forbidden
    prime category - GET success
    prime category - POST/PUT/DELETE success
     */

    @Test
    void unauthorized() {
        this.webClient.get()
                .uri("/customers")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

        this.webClient.get()
                .uri("/customers")
                .header("auth-token", "invalid")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void standardCategory() {
        this.validateGet("secret123", HttpStatus.OK);
    }

    @Test
    void standarCategoryError() {
        this.validatePost("secret123", HttpStatus.FORBIDDEN);
    }

    @Test
    void primeCategory() {
        this.validateGet("secret456", HttpStatus.OK);
    }

    @Test
    void primeCategory1() {
        this.validatePost("secret456", HttpStatus.OK);
    }

    private void validateGet(String token, HttpStatus expectedStatus) {
        this.webClient.get()
                .uri("/customers")
                .header("auth-token", token)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    private void validatePost(String token, HttpStatus expectedStatus) {
        CustomerDTO customerDTO = new CustomerDTO(null, "test", "test@email.com");
        this.webClient.post()
                .uri("/customers")
                .bodyValue(customerDTO)
                .header("auth-token", token)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }
}

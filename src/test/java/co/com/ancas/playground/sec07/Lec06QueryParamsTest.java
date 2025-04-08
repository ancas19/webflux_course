package co.com.ancas.playground.sec07;

import co.com.ancas.playground.sec07.DTO.CalculatorResponse;
import co.com.ancas.playground.sec07.DTO.Product;
import org.apache.commons.logging.Log;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.Objects;

public class Lec06QueryParamsTest extends AbstractWebClient{
    private static final Logger log= LoggerFactory.getLogger(Lec06QueryParamsTest.class);
    private final WebClient client= createWebClient();

    @Test
    void uriBuilderVariables() throws InterruptedException {
        String path="/lec06/calculator";
        String query="first={first}&second={second}&operation={operation}";
        client.get()
                .uri(builder->builder.path(path)
                        .query(query)
                        .build(10,20,"+"))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void uriBuilderMap() throws InterruptedException {
        String path="/lec06/calculator";
        String query="first={first}&second={second}&operation={operation}";
        Map<String, Object> map=Map.of(
                "first",10,
                "second",20,
                "operation","+"
        );
        client.get()
                .uri(builder->builder.path(path)
                        .query(query)
                        .build(map))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}

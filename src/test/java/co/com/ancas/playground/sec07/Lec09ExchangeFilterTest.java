package co.com.ancas.playground.sec07;

import co.com.ancas.playground.sec07.DTO.Product;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.UUID;

public class Lec09ExchangeFilterTest extends AbstractWebClient{
    private static final Logger log= LoggerFactory.getLogger(Lec09ExchangeFilterTest.class);
    private final WebClient client= createWebClient(
            b->b.filter(tokenGenerator())
                    .filter(requestLogger())
    );


    @Test
    void bearer() throws InterruptedException {
        for (int i=0;i<10;i++){
            client.get()
                    .uri("/lec09/product/{id}",i)
                    .attribute("enabled-log",i%2==0)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print())
                    .then()
                    .as(StepVerifier::create)
                    .expectComplete()
                    .verify();
        }

    }

    private ExchangeFilterFunction tokenGenerator(){
        return (request,next)->{
            String token= UUID.randomUUID(). toString().replace("-","");
            log.info(() -> "Token generated: " + token);
            var moodifiedRequest=ClientRequest.from(request).headers(h->h.setBearerAuth(token)).build();
            return next.exchange(moodifiedRequest);
        };
    }

    private ExchangeFilterFunction requestLogger(){
        return (request,next)->{
            boolean isEnabled= (boolean) request.attributes().getOrDefault("enabled-log",false);
            if (isEnabled) log.info(() -> "Request: " + request.method() + " " + request.url());
            return next.exchange(request);
        };
    }
}

package co.com.ancas.playground.sec07;


import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

public abstract class AbstractWebClient {
    private static final Logger log= LoggerFactory.getLogger(AbstractWebClient.class);

    protected  <T> Consumer<T> print(){
        return item->log.info(()->"Item: "+item);
    }

    protected WebClient  createWebClient(Consumer<WebClient.Builder> consumer) {
        WebClient.Builder  builder=WebClient.builder()
                .baseUrl("http://localhost:7070/demo02");
        consumer.accept(builder);
        return builder.build();
    }

    protected WebClient  createWebClient() {
        return this.createWebClient(b->{});
    }
}

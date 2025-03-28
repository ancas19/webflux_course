package co.com.ancas.playground.sec05.filter;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Order(2)
@Component
public class AuthorizationWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Category category = exchange.getAttribute("category");
        return switch (category) {
            case STANDARD -> standardFilter(exchange, chain);
            case PRIME -> primeFilter(exchange, chain);
        };
    }

    private Mono<Void> primeFilter(ServerWebExchange exchange, WebFilterChain chain){
        return chain.filter(exchange);
    }

    private Mono<Void> standardFilter(ServerWebExchange exchange, WebFilterChain chain){
        boolean isGet = HttpMethod.GET.equals(exchange.getRequest().getMethod());
        if(isGet){
            return chain.filter(exchange);
        }
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap("{\"message\":\"Access denied: token is invalid\"}".getBytes(StandardCharsets.UTF_8)))
        );
    }
}

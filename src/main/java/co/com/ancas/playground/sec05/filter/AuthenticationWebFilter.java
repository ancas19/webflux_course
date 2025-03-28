package co.com.ancas.playground.sec05.filter;

import org.springframework.core.annotation.Order;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Order(1)
@Component
public class AuthenticationWebFilter implements WebFilter {
    public static final Map<String, Category> USER_CATEGORIES = Map.of(
            "secret123", Category.STANDARD,
            "secret456", Category.PRIME
    );
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token=exchange.getRequest().getHeaders().getFirst("auth-token");
        if(Objects.isNull(token) || !USER_CATEGORIES.containsKey(token)){
            return Mono.fromRunnable(()->exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED));
        }
        Category category = USER_CATEGORIES.get(token);
        exchange.getAttributes().put("category", category);
        return chain.filter(exchange);
    }
}

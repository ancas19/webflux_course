package co.com.ancas.playground.sec05.filter;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
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
    private final FilterErrorHandler filterErrorHandler;
    public static final Map<String, Category> USER_CATEGORIES = Map.of(
            "secret123", Category.STANDARD,
            "secret456", Category.PRIME
    );

    public AuthenticationWebFilter(FilterErrorHandler filterErrorHandler) {
        this.filterErrorHandler = filterErrorHandler;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token=exchange.getRequest().getHeaders().getFirst("auth-token");
        if(Objects.isNull(token) || !USER_CATEGORIES.containsKey(token)){
           // return Mono.fromRunnable(()->exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED));
            return filterErrorHandler.sendProblemDetail(exchange, HttpStatus.UNAUTHORIZED, "Access denied: token is invalid");
        }
        Category category = USER_CATEGORIES.get(token);
        exchange.getAttributes().put("category", category);
        return chain.filter(exchange);
    }
}

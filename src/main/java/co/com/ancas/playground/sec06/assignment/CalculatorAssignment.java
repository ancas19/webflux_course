package co.com.ancas.playground.sec06.assignment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

import java.util.function.BiFunction;

/*
/calculator/{a}/{b}
header: operation: +,-,/,*
 */
@Configuration
public class CalculatorAssignment {

    @Bean
    public RouterFunction<ServerResponse> calculator() {
        return RouterFunctions.route()
                .path("/calculator",this::calculatorRoutes)
                .build();
    }

    private RouterFunction<ServerResponse> calculatorRoutes() {
        return RouterFunctions.route()
                .GET("/{a}/0",badRequest("b cannot be zero"))
                .GET("/{a}/{b}",isOperation("+"), this.handle((a,b)->a+b))
                .GET("/{a}/{b}",isOperation("-"), this.handle((a,b)->a-b))
                .GET("/{a}/{b}",isOperation("*"), this.handle((a,b)->a*b))
                .GET("/{a}/{b}",isOperation("/"), this.handle((a,b)->a/b))
                .GET("/{a}/{b}",badRequest("operation header should be +,-,*,/"))
                .build();
    }


    private RequestPredicate isOperation(String operation) {
        return RequestPredicates.headers(h->operation.equals(h.firstHeader("operation")));

    }

    private HandlerFunction<ServerResponse> handle(BiFunction<Integer,Integer,Integer> function){
        return req->{
            Integer a = Integer.valueOf(req.pathVariable("a"));
            Integer b = Integer.valueOf(req.pathVariable("b"));
            Integer result = function.apply(a,b);
            return ServerResponse.ok()
                    .bodyValue(result);
        };
    }


    private HandlerFunction<ServerResponse> badRequest(String message) {
        return req->ServerResponse.badRequest()
                .bodyValue(message);
    }
}

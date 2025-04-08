package co.com.ancas.playground.sec06.config;

import co.com.ancas.playground.sec06.exception.CustomerNotFoundException;
import co.com.ancas.playground.sec06.exception.InvalidInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    private final CustomerRequestHandler customerHandler;
    private final CustomControllerAdvice customControllerAdvice;

    public RouterConfiguration(CustomerRequestHandler customerHandler, CustomControllerAdvice customControllerAdvice) {
        this.customerHandler = customerHandler;
        this.customControllerAdvice = customControllerAdvice;
    }

    @Bean
    public RouterFunction<ServerResponse> customerRoutes(){
        return RouterFunctions.route()
                .GET("/customers",customerHandler::allCustomers)
                .GET("/customers/pageable", customerHandler::paginatedCustomers)
                .GET("/customers/{id}",RequestPredicates.path("*/1?"), customerHandler::getCostumer)
                .POST("/customers", customerHandler::saveCostumer)
                .PUT("/customers/{id}", customerHandler::updateCustomer)
                .DELETE("/customers/{id}", customerHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, customControllerAdvice::customerNotFoundException)
                .onError(InvalidInputException.class, customControllerAdvice::customInvalidInputException)
                .build();
    }


    private RouterFunction<ServerResponse> customerRoutes2(){
        return RouterFunctions.route()
                .GET("/pageable", customerHandler::paginatedCustomers)
                .GET("/{id}", customerHandler::getCostumer)
                .GET(customerHandler::allCustomers)
                .build();
    }
}

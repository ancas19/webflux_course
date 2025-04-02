package co.com.ancas.playground.sec06.config;

import co.com.ancas.playground.sec06.service.CustomerService;
import co.com.ancas.playground.sec06.exception.CustomerNotFoundException;
import co.com.ancas.playground.sec06.dto.CustomerDTO;
import co.com.ancas.playground.sec06.validator.RequestValidator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.ancas.playground.sec06.enums.Messages.MESSAGE_ERROR_USER_NOT_FOUND;

@Service
public class CustomerRequestHandler {
    private final CustomerService customerService;

    public CustomerRequestHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Mono<ServerResponse> paginatedCustomers(ServerRequest serverRequest) {
        Integer page = Integer.valueOf(serverRequest.queryParam("page").orElse("0"));
        Integer size = Integer.valueOf(serverRequest.queryParam("size").orElse("10"));
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return this.customerService
                .findAllPageable(pageable)
                .as(flux-> ServerResponse.ok().body(flux, CustomerDTO.class));

    }
    public Mono<ServerResponse> allCustomers(ServerRequest serverRequest) {
        //request.pathVariable()
        //request.headers()
        //request.queryParam()
        return this.customerService
                .findAll()
                .as(flux-> ServerResponse.ok().body(flux, CustomerDTO.class));
    }
    public Mono<ServerResponse> getCostumer(ServerRequest serverRequest) {
        return this.customerService
                .findById(Long.valueOf(serverRequest.pathVariable("id")))
                .switchIfEmpty(
                        Mono.error(new CustomerNotFoundException(MESSAGE_ERROR_USER_NOT_FOUND.getMessage().formatted(serverRequest.pathVariable("id"))))
                )
                .flatMap(ServerResponse.ok()::bodyValue);
    }
    public Mono<ServerResponse> saveCostumer(ServerRequest serverRequest) {
       return serverRequest.bodyToMono(CustomerDTO.class)
                .transform(RequestValidator.validate())
                .as(this.customerService::save)
                .flatMap(ServerResponse.ok()::bodyValue);

    }

    public Mono<ServerResponse> updateCustomer(ServerRequest serverRequest) {
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        return serverRequest.bodyToMono(CustomerDTO.class)
                .transform(RequestValidator.validate())
                .as(data->this.customerService.updateCustomer(id,data))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest serverRequest) {
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        return this.customerService.deleteCustomer(id)
                .filter(b->b)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(MESSAGE_ERROR_USER_NOT_FOUND.getMessage().formatted(serverRequest.pathVariable("id")))))
                .then(ServerResponse.ok().build());
    }
}

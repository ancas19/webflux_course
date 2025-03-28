package co.com.ancas.playground.sec05.controller;

import co.com.ancas.playground.sec05.dto.CustomerDTO;
import co.com.ancas.playground.sec05.enums.Messages;
import co.com.ancas.playground.sec05.exception.CustomerNotFoundException;
import co.com.ancas.playground.sec05.service.CustomerService;
import co.com.ancas.playground.sec05.validator.RequestValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class  CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public Flux<CustomerDTO> findAll() {
        return this.customerService.findAll();
    }

    @GetMapping("/pageable")
    public  Mono<List<CustomerDTO>> findAllPagebale(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size
    ) {
        return this.customerService.findAllPageable(PageRequest.of(page, size))
                .collectList();
    }

    @GetMapping("/{id}")
    public Mono<CustomerDTO> findById(@PathVariable("id") Long id) {
        return this.customerService.findById(id);
    }

    @PostMapping
    public Mono<CustomerDTO> save(@RequestBody Mono<CustomerDTO> customerDTO) {
        return customerDTO
                .transform(RequestValidator.validate())
                .as(this.customerService::save);
    }

    @PutMapping("/{id}")
    public Mono<CustomerDTO>  updateCustomer(@PathVariable("id") Long id,@RequestBody Mono<CustomerDTO> customerDTO) {
        return customerDTO
                .transform(RequestValidator.validate())
                .as(customerDTOMono -> this.customerService.updateCustomer(id, customerDTOMono));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable("id") Long id) {
        return this.customerService.deleteCustomer(id)
                .filter(b->b)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(Messages.MESSAGE_ERROR_USER_NOT_FOUND.getMessage().formatted(id))))
                .then();
    }
}

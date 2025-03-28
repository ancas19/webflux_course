package co.com.ancas.playground.sec03.controller;

import co.com.ancas.playground.sec03.dto.CustomerDTO;
import co.com.ancas.playground.sec03.service.CustomerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
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
    public Mono<ResponseEntity<CustomerDTO>> findById(@PathVariable("id") Long id) {
        return this.customerService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<CustomerDTO> save(@RequestBody Mono<CustomerDTO> customerDTO) {
        return this.customerService.save(customerDTO);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>>  updateCustomer(@PathVariable("id") Long id,@RequestBody Mono<CustomerDTO> customerDTO) {
        return this.customerService.updateCustomer(id, customerDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable("id") Long id) {
        return this.customerService.deleteCustomer(id)
                .filter(b->b)
                .map(b->ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

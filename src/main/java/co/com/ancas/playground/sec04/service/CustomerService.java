package co.com.ancas.playground.sec04.service;

import co.com.ancas.playground.sec04.dto.CustomerDTO;
import co.com.ancas.playground.sec04.enums.Messages;
import co.com.ancas.playground.sec04.exception.CustomerNotFoundException;
import co.com.ancas.playground.sec04.mapper.EntityDTOMapper;
import co.com.ancas.playground.sec04.repository.CustomerRepository;
import co.com.ancas.playground.sec04.validator.RequestValidator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Flux<CustomerDTO> findAll() {
        return this.customerRepository.findAll()
                .map(EntityDTOMapper::toDTO);
    }

    public Flux<CustomerDTO> findAllPageable(Pageable pageable) {
        return this.customerRepository.findBy(pageable)
                .map(EntityDTOMapper::toDTO);
    }

    public Mono<CustomerDTO> findById(Long id) {
        return this.customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(Messages.MESSAGE_ERROR_USER_NOT_FOUND.getMessage().formatted(id))))
                .map(EntityDTOMapper::toDTO);
    }

    public Mono<CustomerDTO> save(Mono<CustomerDTO> customerDTO) {
        return customerDTO// we should do input validation. let's worry about it later.
                .map(EntityDTOMapper::toEntity)
                .flatMap(this.customerRepository::save)
                .map(co.com.ancas.playground.sec04.mapper.EntityDTOMapper::toDTO);
    }

    public Mono<CustomerDTO> updateCustomer(Long id, Mono<CustomerDTO> customerDTO) {
        return this.customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(Messages.MESSAGE_ERROR_USER_NOT_FOUND.getMessage().formatted(id))))
                .zipWith(customerDTO,(entity,dto)->{
                    entity.setName(dto.name());
                    entity.setEmail(dto.email());
                    return entity;
                })
                .flatMap(this.customerRepository::save)
                .map(EntityDTOMapper::toDTO);
    }

    public Mono<Boolean> deleteCustomer(Long id) {
        return this.customerRepository
                .deleteCustomerById(id);
    }
}

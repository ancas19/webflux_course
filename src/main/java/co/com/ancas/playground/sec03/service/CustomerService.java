package co.com.ancas.playground.sec03.service;

import co.com.ancas.playground.sec03.dto.CustomerDTO;
import co.com.ancas.playground.sec03.mapper.EntityDTOMapper;
import co.com.ancas.playground.sec03.repository.CustomerRepository;
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

    public Mono<CustomerDTO> findById(Long id) {
        return this.customerRepository.findById(id)
                .map(EntityDTOMapper::toDTO);
    }

    public Mono<CustomerDTO> save(Mono<CustomerDTO> customerDTO) {
        return customerDTO// we should do input validation. let's worry about it later.
                .map(EntityDTOMapper::toEntity)
                .flatMap(this.customerRepository::save)
                .map(EntityDTOMapper::toDTO);
    }

    public Mono<CustomerDTO> updateCustomer(Long id, Mono<CustomerDTO> customerDTO) {
        return this.customerRepository.findById(id)
                .zipWith(customerDTO,(entity,dto)->{
                    entity.setName(dto.name());
                    entity.setEmail(dto.email());
                    return entity;
                })
                .flatMap(this.customerRepository::save)
                .map(EntityDTOMapper::toDTO);
    }
}

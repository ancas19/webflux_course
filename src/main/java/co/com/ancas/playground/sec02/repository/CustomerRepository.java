package co.com.ancas.playground.sec02.repository;

import co.com.ancas.playground.sec02.entity.CustomerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<CustomerEntity, Long> {
    Flux<CustomerEntity> findByName(String name);
    Flux<CustomerEntity> findByEmailEndingWith(String email);
}

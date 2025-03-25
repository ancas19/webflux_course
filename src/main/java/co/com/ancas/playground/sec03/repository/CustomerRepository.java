package co.com.ancas.playground.sec03.repository;

import co.com.ancas.playground.sec03.entity.CustomerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CustomerRepository extends ReactiveCrudRepository<CustomerEntity, Long> {
}

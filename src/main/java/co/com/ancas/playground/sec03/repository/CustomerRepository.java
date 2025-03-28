package co.com.ancas.playground.sec03.repository;

import co.com.ancas.playground.sec03.entity.CustomerEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<CustomerEntity, Long> {

    @Modifying
    @Query("DELETE FROM customer WHERE id = :id")
    Mono<Boolean> deleteCustomerById(@Param("id") Long id);

    Flux<CustomerEntity> findBy(Pageable pageable);
}

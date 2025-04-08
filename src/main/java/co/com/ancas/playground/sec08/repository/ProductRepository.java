package co.com.ancas.playground.sec08.repository;

import co.com.ancas.playground.sec08.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
}

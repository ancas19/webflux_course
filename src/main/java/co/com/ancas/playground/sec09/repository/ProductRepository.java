package co.com.ancas.playground.sec09.repository;

import co.com.ancas.playground.sec09.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
}

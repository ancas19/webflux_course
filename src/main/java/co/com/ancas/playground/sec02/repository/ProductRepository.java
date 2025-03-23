package co.com.ancas.playground.sec02.repository;

import co.com.ancas.playground.sec02.entity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {

    Flux<ProductEntity> findByPriceBetween(Integer start, Integer end);
    Flux <ProductEntity> findBy(Pageable pageable);
}

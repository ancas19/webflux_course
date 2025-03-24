package co.com.ancas.playground.sec02.repository;

import co.com.ancas.playground.sec01.Product;
import co.com.ancas.playground.sec02.dto.OrderDetails;
import co.com.ancas.playground.sec02.entity.CustomerOrderEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface CustomerOrderRepository extends ReactiveCrudRepository<CustomerOrderEntity, UUID> {

    @Query("""
            SELECT
                p.*
            FROM
                customer c
            INNER JOIN customer_order co ON c.id = co.customer_id
            INNER JOIN product p ON co.product_id = p.id
            WHERE
                c.id = :id
            """)
    Flux<Product> finOrdersdByCustomerId(@Param("id") Long customerId);


     @Query(
             """
                     SELECT
                         co.order_id,
                         c.name AS customer_name,
                         p.description AS product_name,
                         co.amount,
                         co.order_date
                     FROM
                         customer c
                     INNER JOIN customer_order co ON c.id = co.customer_id
                     INNER JOIN product p ON p.id = co.product_id
                     WHERE
                         p.id = :id
                     ORDER BY co.amount DESC
             """
     )
    Flux<OrderDetails> findOrderDetailsByProductId(@Param("id") Long productId);
}

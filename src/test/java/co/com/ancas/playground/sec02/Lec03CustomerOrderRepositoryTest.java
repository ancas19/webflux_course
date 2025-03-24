package co.com.ancas.playground.sec02;

import co.com.ancas.playground.sec02.repository.CustomerOrderRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class Lec03CustomerOrderRepositoryTest  extends AbstractTest{
    private static final Logger log = LoggerFactory.getLogger(Lec03CustomerOrderRepositoryTest.class);

    @Autowired
    private CustomerOrderRepository customerOrderRepository;


    @Test
    void productOrderedByCustomer() {
        this.customerOrderRepository.finOrdersdByCustomerId(1L)
                .doOnNext(customerOrder -> log.info("Customer Order: {}", customerOrder))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }


    @Test
    void findOrderDetailsByProductId() {
        this.customerOrderRepository.findOrderDetailsByProductId(1L)
                .doOnNext(orderDetails -> log.info("Order Details: {}", orderDetails))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }
}

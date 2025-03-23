package co.com.ancas.playground.sec02;

import co.com.ancas.playground.sec02.entity.CustomerEntity;
import co.com.ancas.playground.sec02.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class Lec01CustomerRepositoryTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(Lec01CustomerRepositoryTest.class);

    @Autowired
    private CustomerRepository customerRepository;


    @Test
    void findAll() {
        this.customerRepository.findAll()
                .doOnNext(customer -> log.info("Customer: {}", customer))
                .as(StepVerifier::create)
                .expectNextCount(10)
                .expectComplete()
                .verify();
    }

    @Test
    void findById() {
        this.customerRepository.findById(2L)
                .doOnNext(customer -> log.info("Customer: {}", customer))
                .as(StepVerifier::create)
                .assertNext(customerEntity -> assertEquals("mike", customerEntity.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    void findByName(){
        this.customerRepository.findByName("mike")
                .as(StepVerifier::create)
                .assertNext(customerEntity -> assertEquals("mike@gmail.com", customerEntity.getEmail()))
                .expectComplete()
                .verify();
    }

    /*
    Query methods
    https://docs.spring.io/spring-data/relational/reference/r2dbc/query-methods.html

    Task: find all customer whose email ending with "ke@gmail.com"
     */

    @Test
    void findByEmail(){
        this.customerRepository.findByEmailEndingWith("ke@gmail.com")
                .as(StepVerifier::create)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    void insertAndDeleteCustomer(){
        //insert
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName("marshal");
        customerEntity.setEmail("marshal@gmail");

        customerRepository.save(customerEntity)
                .doOnNext(c->log.info("Inserted customer: {}", c))
                .as(StepVerifier::create)
                .assertNext(customerSaved->assertNotNull(customerSaved.getId()))
                .expectComplete()
                .verify();
        //coun
        this.customerRepository.count()
                .as(StepVerifier::create)
                .assertNext(count -> assertEquals(11, count))
                .expectComplete()
                .verify();

        //delete
        customerRepository.deleteById(11L)
                .then(this.customerRepository.count())
                .as(StepVerifier::create)
                .assertNext(count -> assertEquals(10, count))
                .expectComplete()
                .verify();

    }


    @Test
    void updateCustomer(){
        this.customerRepository.findByName("ethan")
                .doOnNext(c->c.setName("noel"))
                .flatMap(c->customerRepository.save(c))
                .doOnNext(c->log.info("Updated customer: {}", c))
                .as(StepVerifier::create)
                .assertNext(customerEntity -> assertEquals("noel", customerEntity.getName()))
                .expectComplete()
                .verify();
    }
}

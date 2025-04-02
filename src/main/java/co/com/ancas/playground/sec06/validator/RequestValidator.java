package co.com.ancas.playground.sec06.validator;

import co.com.ancas.playground.sec06.dto.CustomerDTO;
import co.com.ancas.playground.sec06.enums.Messages;
import co.com.ancas.playground.sec06.exception.InvalidInputException;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    public static UnaryOperator<Mono<CustomerDTO>> validate(){
        return customerDTO -> customerDTO
                .filter(hasName())
                .switchIfEmpty(Mono.error(new InvalidInputException(Messages.MESSAGE_ERROR_NAME_REQUIRED.getMessage())))
                .filter(hasEmail())
                .switchIfEmpty(Mono.error(new InvalidInputException(Messages.MESSAGE_ERROR_EMAIL_REQUIRED.getMessage())));
    }
    private static Predicate<CustomerDTO> hasName() {
        return customerDTO -> Objects.nonNull(customerDTO.name()) && !customerDTO.name().isBlank();
    }

    private static Predicate<CustomerDTO> hasEmail() {
        return customerDTO -> Objects.nonNull(customerDTO.email()) && customerDTO.email().contains("@");
    }
}

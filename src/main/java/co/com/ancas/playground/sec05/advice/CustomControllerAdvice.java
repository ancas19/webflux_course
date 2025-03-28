package co.com.ancas.playground.sec05.advice;

import co.com.ancas.playground.sec05.exception.CustomerNotFoundException;
import co.com.ancas.playground.sec05.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class CustomControllerAdvice {
    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail customerNotFoundException(CustomerNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setType(URI.create("http://ancas.co/errors/not-found"));
        problemDetail.setTitle("Customer not found");
        return problemDetail;
    }

    @ExceptionHandler(InvalidInputException.class)
    public ProblemDetail customInvalidInputException(InvalidInputException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setType(URI.create("http://ancas.co/errors/invalid-input"));
        problemDetail.setTitle("Invalid input");
        return problemDetail;
    }
}

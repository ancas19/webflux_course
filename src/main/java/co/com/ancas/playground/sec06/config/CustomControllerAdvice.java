package co.com.ancas.playground.sec06.config;

import co.com.ancas.playground.sec06.exception.CustomerNotFoundException;
import co.com.ancas.playground.sec06.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;

@Component
public class CustomControllerAdvice {
    public Mono<ServerResponse> customerNotFoundException(CustomerNotFoundException exception, ServerRequest request) {
        return handleException(
                HttpStatus.NOT_FOUND,
                exception,
                request,
                problemDetail -> {
                    problemDetail.setTitle("Customer not found");
                    problemDetail.setType(URI.create("http://ancas.co/errors/not-found"));
                }
        );
    }


    public Mono<ServerResponse> customInvalidInputException(InvalidInputException exception, ServerRequest request) {
        return handleException(
                HttpStatus.BAD_REQUEST,
                exception,
                request,
                problemDetail -> {
                    problemDetail.setTitle("Invalid input");
                    problemDetail.setType(URI.create("http://ancas.co/errors/not-found"));
                }
        );
    }

    private Mono<ServerResponse> handleException(HttpStatus status, Exception ex, ServerRequest request, Consumer<ProblemDetail> consumer) {
       ProblemDetail problemDetail=ProblemDetail
               .forStatusAndDetail(status, ex.getMessage());
       problemDetail.setInstance(URI.create(request.path()));
        consumer.accept(problemDetail);
        return ServerResponse
                .status(status)
                .bodyValue(problemDetail);
    }

}

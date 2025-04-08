package co.com.ancas.playground.sec07.DTO;

public record CalculatorResponse(
        Integer first,
        Integer second,
        String operation,
        Integer result
) {
}

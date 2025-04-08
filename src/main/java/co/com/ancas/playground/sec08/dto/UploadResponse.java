package co.com.ancas.playground.sec08.dto;

import java.util.UUID;

public record UploadResponse(
        UUID confirmartionId,
        Long productsCount
) {
}

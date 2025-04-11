package co.com.ancas.playground.sec09.dto;

import java.util.UUID;

public record UploadResponse(
        UUID confirmartionId,
        Long productsCount
) {
}

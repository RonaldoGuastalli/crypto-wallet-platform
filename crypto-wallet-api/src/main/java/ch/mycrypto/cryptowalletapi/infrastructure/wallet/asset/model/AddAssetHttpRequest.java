package ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AddAssetHttpRequest(
        @NotBlank(message = "Symbol is required")
        String symbol,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0001", message = "Price must be positive")
        BigDecimal price,

        @NotNull(message = "Quantity is required")
        @DecimalMin(value = "0.0001", message = "Quantity must be positive")
        BigDecimal quantity
) {
}
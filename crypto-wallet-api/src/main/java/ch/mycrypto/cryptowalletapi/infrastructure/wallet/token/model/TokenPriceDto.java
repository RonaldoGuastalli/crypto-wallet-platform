package ch.mycrypto.cryptowalletapi.infrastructure.wallet.token.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TokenPriceDto(
        String id,
        String symbol,
        BigDecimal price,
        LocalDateTime timestamp
) {
}
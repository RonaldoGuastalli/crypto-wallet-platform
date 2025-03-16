package ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.model;

import java.math.BigDecimal;

public record AssetDto(
        String symbol,
        BigDecimal quantity,
        BigDecimal price
) {
}
